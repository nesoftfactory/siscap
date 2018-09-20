ALTER TABLE dbo.fonte ALTER COLUMN usuario_criacao int;

ALTER TABLE dbo.fonte ALTER COLUMN usuario_atualizacao int;

ALTER TABLE dbo.fonte ADD CONSTRAINT
	DF_usuario_criacao_f FOREIGN KEY (usuario_criacao) REFERENCES dbo.usuario(id)
GO
ALTER TABLE dbo.fonte ADD CONSTRAINT
	DF_usuario_atualizacao_f FOREIGN KEY (usuario_atualizacao) REFERENCES dbo.usuario(id)
GO

INSERT INTO fonte (nome, url, id_tipo_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial dos municípios', 'http://diarioficialdosmunicipios.org', (select tf.id from tipo_fonte tf where nome = 'Diário Oficial Municipal'),  getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)

INSERT INTO fonte (nome, url, id_tipo_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial do Estado', 'http://www.diariooficial.pi.gov.br', (select tf.id from tipo_fonte tf where nome = 'Diário Oficial Estadual'),  getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)

INSERT INTO fonte (nome, url, id_tipo_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial de Teresina', 'http://www.dom.teresina.pi.gov.br', (select tf.id from tipo_fonte tf where nome = 'Diário Oficial Municipal'),  getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)

INSERT INTO fonte (nome, url, id_tipo_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial de Parnaíba', 'http://dom.parnaiba.pi.gov.br', (select tf.id from tipo_fonte tf where nome = 'Diário Oficial Municipal'),  getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)

INSERT INTO fonte (nome, url, id_tipo_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial dos municípios', 'http://diarioficialdosmunicipios.org', (select tf.id from tipo_fonte tf where nome = 'Diário Oficial Municipal'),  getdate(), (select u.id from usuario u where login = 'adminAut'), getdate(), (select u.id from usuario u where login = 'adminAut'), 1)

	
