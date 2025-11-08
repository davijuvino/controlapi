package br.com.controlapi.service;


import br.com.controlapi.model.dto.UserDTO;
import br.com.controlapi.model.entity.Authority;
import br.com.controlapi.model.entity.User;
import br.com.controlapi.model.exception.EmailAlreadyUsedException;
import br.com.controlapi.model.exception.InvalidPasswordException;
import br.com.controlapi.model.exception.LoginAlreadyInUseException;
import br.com.controlapi.repository.AuthorityRepository;
import br.com.controlapi.repository.UserRepository;
import br.com.controlapi.security.AuthorityConstants;
import br.com.controlapi.security.SecurityUtils;
import br.com.controlapi.service.util.RandomUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final CacheManager cacheManager;

	@Autowired
	public UserService(UserRepository userRepository, AuthorityRepository authorityRepository,
					   PasswordEncoder passwordEncoder,
					   EmailService emailService, CacheManager cacheManager) {
		this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
        this.cacheManager = cacheManager;
    }

	public Optional<User> activateRegistration(String key) {
		log.debug("Ativando o usuário para a chave de ativação {}", key);
		return userRepository.findOneByActivationKey(key)
				.map(user -> {
					user.setActivated(true);
					user.setActivationKey(null);
					this.cleanUserCaches(user);
					log.debug("User ativado: {}", user);
					return user;
				});
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userRepository.findOneByEmailIgnoreCase(mail)
				.filter(User::isActivated)
				.map(user -> {
					user.setResetKey(RandomUtil.generateResetKey());
					user.setResetDate(Instant.now());
					this.cleanUserCaches(user);
					return user;
				});
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Redefina a senha do usuário para a chave de redefinição. {}", key);
		return userRepository.findOneByResetKey(key)
				.filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
				.map(user -> {
					user.setPassword(passwordEncoder.encode(newPassword));
					user.setResetKey(null);
					user.setResetDate(null);
					this.cleanUserCaches(user);
					return user;
				});
	}

    public User registerUser(UserDTO userDTO, String password) {
		userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existsUser -> {
			if (!existsUser.isActivated()) {
				userRepository.delete(existsUser);
				this.cleanUserCaches(existsUser);
			} else {
				throw new LoginAlreadyInUseException();
			}
		});
		userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existsUser -> {
			if (!existsUser.isActivated()) {
				userRepository.delete(existsUser);
				userRepository.flush();
				this.cleanUserCaches(existsUser);
			} else {
				throw new EmailAlreadyUsedException();
			}
		});
		User newUser = new User();
		String encryptedPassword = passwordEncoder.encode(password);
		newUser.setLogin(userDTO.getLogin().toLowerCase());
		// ao inicialiar o novo usuario, gerar a password criptografada
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setEmail(userDTO.getEmail().toLowerCase());
		newUser.setLangKey(userDTO.getLangKey());
		// novo usuario nao esta ativado
		newUser.setActivated(false);
		// novo usuario registrar a chave
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		Set<Authority> authorities = new HashSet<>();
		authorityRepository.findById(AuthorityConstants.USUARIO).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		this.cleanUserCaches(newUser);
		log.info("Criar informações para usuario: {}", newUser);
		return newUser;
    }

	/**
	 * Atualize as informações básicas (nome, sobrenome, e-mail, idioma) do usuário atual.
	 *
	 * @param firstName primeiro nome do usuário
	 * @param lastName sobrenome do usuário
	 * @param email Endereço de e-mail do usuário
	 * @param langKey chave de idioma
	 */
	public void updateUser(String firstName, String lastName, String email, String langKey) {
		SecurityUtils.getCurrentUserLogin()
				.flatMap(userRepository::findOneByLogin)
				.ifPresent(user -> {
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setEmail(email.toLowerCase());
					user.setLangKey(langKey);
					this.cleanUserCaches(user);
					log.debug("Troca de informações do usuário para {}", user);
				});
	}

	/**
	 * Atualize todas as informações de um usuário específico e retorne o usuário modificado.
	 *
	 * @param userDTO usuário para atualizar
	 * @return usuário atualizado
	 */
	public Optional<UserDTO> updateUser(UserDTO userDTO) {
		return Optional.of(userRepository
				.findById(userDTO.getId()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(user -> {
					this.cleanUserCaches(user);
					user.setLogin(userDTO.getLogin().toLowerCase());
					user.setFirstName(userDTO.getFirstName());
					user.setLastName(userDTO.getLastName());
					user.setEmail(userDTO.getEmail().toLowerCase());
					user.setActivated(userDTO.isActivated());
					user.setLangKey(userDTO.getLangKey());
					Set<Authority> managedAuthorities = user.getAuthorities();
					managedAuthorities.clear();
					userDTO.getAuthorities().stream()
							.map(authorityRepository::findById)
							.filter(Optional::isPresent)
							.map(Optional::get)
							.forEach(managedAuthorities::add);
					this.cleanUserCaches(user);
					log.debug("Troca de informações do usuário: {}", user);
					return user;
				})
				.map(UserDTO::new);
	}

	public void changePassword(String currentClearTextPassword, String newPassword) {
		SecurityUtils.getCurrentUserLogin()
				.flatMap(userRepository::findOneByLogin)
				.ifPresent(user -> {
					String currentEncryptedPassword = user.getPassword();
					if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
						throw new InvalidPasswordException();
					}
					String encryptedPassword = passwordEncoder.encode(newPassword);
					user.setPassword(encryptedPassword);
					this.cleanUserCaches(user);
					log.debug("Alterar a senha do usuário: {}", user);
				});
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities() {
		return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
	}

	private void cleanUserCaches(User user) {
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
	}
}