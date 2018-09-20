delete from usuario;

ALTER TABLE dbo.usuario DROP COLUMN cpf;

ALTER TABLE dbo.usuario ALTER COLUMN usuario_criacao int;

ALTER TABLE dbo.usuario ALTER COLUMN usuario_atualizacao int;

ALTER TABLE dbo.usuario ADD CONSTRAINT
	DF_usuario_criacao FOREIGN KEY (usuario_criacao) REFERENCES dbo.usuario(id)
GO
ALTER TABLE dbo.usuario ADD CONSTRAINT
	DF_usuario_atualizacao FOREIGN KEY (usuario_atualizacao) REFERENCES dbo.usuario(id)
GO
	
INSERT INTO usuario (nome, login, admin, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Administrador Automático', 'adminAut', 1,  getdate(), null, getdate(), null, 1);

	
INSERT INTO usuario (nome, login, admin, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Administrador', 'adminInicial', 1,  getdate(),(select u.id from usuario u where login = 'adminAut') , getdate(), (select u.id from usuario u where login = 'adminAut'), 1);
