-- Script para criar as tabelas do banco de dados
DROP TABLE usuarios IF EXISTS;
CREATE TABLE `usuarios` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) DEFAULT NULL,
  `email` VARCHAR(50) DEFAULT NULL,
  `login` VARCHAR(50) DEFAULT NULL,
  `senha` VARCHAR(50) DEFAULT NULL,
  `atualizado_at` DATETIME DEFAULT NULL,
  `criado_at` DATETIME NOT NULL,
  `deletado_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE despesas IF EXISTS ;
CREATE TABLE `despesas` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) DEFAULT NULL,
  `categoria` VARCHAR(100) DEFAULT NULL,
  `descricao` VARCHAR(255) DEFAULT NULL,
  `criado_at` DATETIME NOT NULL,
  `atualizado_at` DATETIME DEFAULT NULL,
  `deletado_at` DATETIME DEFAULT NULL,
  `usuario_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE produto IF EXISTS ;
CREATE TABLE `produto` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `quantidade` INT DEFAULT NULL,
  `preco` DECIMAL(38,2) DEFAULT NULL,
  `data_at` DATE DEFAULT NULL,
  `criado_at` DATETIME NOT NULL,
  `atualizado_at` DATETIME DEFAULT NULL,
  `deletado_at` DATETIME DEFAULT NULL,
  `despesa_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE npl_perfil IF EXISTS ;
CREATE TABLE npl_perfil (
    id BIGINT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

DROP TABLE npl_perfil_permissao IF EXISTS ;
CREATE TABLE npl_perfil_permissao (
    id BIGINT PRIMARY KEY,
    perfil_id BIGINT NOT NULL,
    recurso_id BIGINT NOT NULL
);

DROP TABLE npl_perfil_usuario IF EXISTS ;
CREATE TABLE npl_perfil_usuario (
    id BIGINT PRIMARY KEY,
    perfil_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL
);

DROP TABLE npl_usuario_verificador IF EXISTS ;
CREATE TABLE npl_usuario_verificador (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_expiracao TIMESTAMP(6) NOT NULL,
    uuid CHAR(36) NOT NULL,
    usuario_id BIGINT NOT NULL
);

DROP TABLE recurso IF EXISTS ;
CREATE TABLE recurso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chave_id VARCHAR(255),
    nome VARCHAR(255)
);


-- Padrão fk_[tabelaorigem]_[tabeladestino]


-- Adiciona FK entre despesas e usuarios
ALTER TABLE IF EXISTS despesas
ADD CONSTRAINT fk_despesas_usuario
FOREIGN KEY (usuario_id)
REFERENCES usuarios;

-- Adiciona FK entre npl_perfil_permissao e npl_perfil (perfil_id)
ALTER TABLE IF EXISTS npl_perfil_permissao
ADD CONSTRAINT fk_perfilpermissao_perfil
FOREIGN KEY (perfil_id)
REFERENCES npl_perfil;

-- Adiciona FK entre npl_perfil_permissao e recurso (recurso_id)
ALTER TABLE IF EXISTS npl_perfil_permissao
ADD CONSTRAINT fk_perfilpermissao_recurso
FOREIGN KEY (recurso_id)
REFERENCES recurso;

-- Adiciona FK entre npl_perfil_usuario e npl_perfil (perfil_id)
ALTER TABLE IF EXISTS npl_perfil_usuario
ADD CONSTRAINT fk_perfilusuario_perfil
FOREIGN KEY (perfil_id)
REFERENCES npl_perfil;

-- Adiciona FK entre npl_perfil_usuario e usuarios (usuario_id)
ALTER TABLE IF EXISTS npl_perfil_usuario
ADD CONSTRAINT fk_perfilusuario_usuario
FOREIGN KEY (usuario_id)
REFERENCES usuarios;

-- Adiciona FK entre npl_usuario_verificador e usuarios (usuario_id)
ALTER TABLE IF EXISTS npl_usuario_verificador
ADD CONSTRAINT fk_verificador_usuario
FOREIGN KEY (usuario_id)
REFERENCES usuarios;

-- Adiciona FK entre produto e despesas (despesa_id)
ALTER TABLE IF EXISTS produto
ADD CONSTRAINT fk_produto_despesa
FOREIGN KEY (despesa_id)
REFERENCES despesas;;



