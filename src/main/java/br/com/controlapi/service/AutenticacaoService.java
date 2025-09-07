package br.com.controlapi.service;

import br.com.controlapi.dto.AcessoDTO;
import br.com.controlapi.dto.AutenticacaoDTO;
import br.com.controlapi.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;



@Service
public class AutenticacaoService {

	@Autowired
	private AuthenticationManager authenticatioManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	public AcessoDTO login(AutenticacaoDTO authDto) {
		
		try {
		//Cria mecanismo de credencial para o spring
		UsernamePasswordAuthenticationToken userAuth = 
				new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
		
		//Prepara mecanismo para autenticacao
		Authentication authentication = authenticatioManager.authenticate(userAuth);
		
		//Busca usuario logado
		UserDetailsImpl userAuthenticate = (UserDetailsImpl)authentication.getPrincipal();
		
		String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);
		
		AcessoDTO accessDto = new AcessoDTO(token);
		
		return accessDto;
		
		}catch(BadCredentialsException e) {
			//TODO LOGIN OU SENHA INVALIDO
		}
		return new AcessoDTO("Acesso negado");
	}
}
