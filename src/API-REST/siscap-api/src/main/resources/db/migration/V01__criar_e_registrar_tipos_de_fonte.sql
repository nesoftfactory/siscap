CREATE TABLE dbo.tipo_fonte
	(
	id int NOT NULL IDENTITY (1, 1),
	nome varchar(50) NOT NULL,
	data_criacao datetime NOT NULL,
	usuario_criacao varchar(50) NOT NULL,
	data_atualizacao datetime NOT NULL,
	usuario_atualizacao varchar(50) NOT NULL,
	ativo bit NOT NULL
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.tipo_fonte ADD CONSTRAINT
	PK_tipo_fonte PRIMARY KEY CLUSTERED 
	(
	id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
ALTER TABLE dbo.tipo_fonte ADD CONSTRAINT
	DF_tipo_fonte_ativo DEFAULT 1 FOR ativo
GO
	
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial Municipal', getdate(), 'sistema', getdate(), 'sistema', 1)
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial Estadual', getdate(), 'sistema', getdate(), 'sistema', 1)
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial Federal', getdate(), 'sistema', getdate(), 'sistema', 1)
INSERT INTO tipo_fonte (nome, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Extrato bancário', getdate(), 'sistema', getdate(), 'sistema', 1)
GO
