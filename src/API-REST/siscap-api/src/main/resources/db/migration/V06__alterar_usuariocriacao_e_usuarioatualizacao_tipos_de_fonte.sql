delete from fonte;
delete from tipo_fonte;

ALTER TABLE dbo.tipo_fonte ALTER COLUMN usuario_criacao int;

ALTER TABLE dbo.tipo_fonte ALTER COLUMN usuario_atualizacao int;

ALTER TABLE dbo.tipo_fonte ADD CONSTRAINT
	DF_usuario_criacao_tf FOREIGN KEY (usuario_criacao) REFERENCES dbo.usuario(id)
GO
ALTER TABLE dbo.tipo_fonte ADD CONSTRAINT
	DF_usuario_atualizacao_tf FOREIGN KEY (usuario_atualizacao) REFERENCES dbo.usuario(id)
GO
	
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial Municipal', getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial Estadual', getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial Federal', getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Extrato bancário', getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)
GO
