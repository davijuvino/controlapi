package br.com.controlapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditoriaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "criado_por", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String criadoPor;

    @CreatedDate
    @Column(name = "criado_data", nullable = false, updatable = false)
    @JsonIgnore
    private Instant criadoData = Instant.now();

    @LastModifiedBy
    @Column(name = "ultima_modificacao_por", length = 50)
    @JsonIgnore
    private String ultimaModificacaoPor;

    @LastModifiedDate
    @Column(name = "ultima_modificacao_data")
    @JsonIgnore
    private Instant ultimaModificacaoData = Instant.now();

}
