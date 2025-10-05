package br.com.controlapi.repository;

import br.com.controlapi.model.entity.Autorizacoes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorizacoesRepository extends JpaRepository<Autorizacoes, String> {
}
