package br.com.controlapi.service;


import br.com.controlapi.model.dto.UserDTO;
import br.com.controlapi.model.entity.Authority;
import br.com.controlapi.model.entity.User;
import br.com.controlapi.model.exception.EmailAlreadyInUseException;
import br.com.controlapi.model.exception.LoginAlreadyInUseException;
import br.com.controlapi.repository.AuthorityRepository;
import br.com.controlapi.repository.UserRepository;
import br.com.controlapi.security.AuthorityConstants;
import br.com.controlapi.service.util.RandomUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
					// ativar o usuário fornecido para a chave de registro.
					user.setActivated(true);
					user.setActivationKey(null);
					this.cleanUserCaches(user);
					log.debug("User ativado: {}", user);
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
				throw new EmailAlreadyInUseException();
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

	private void cleanUserCaches(User user) {
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
		Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
	}
}