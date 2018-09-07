CREATE TABLE dbo.fonte
	(
	id int NOT NULL IDENTITY (1, 1),
	nome varchar(50) NOT NULL,
	url varchar(100) NULL,
	id_tipo_fonte int NOT NULL,
	data_criacao datetime NOT NULL,
	usuario_criacao varchar(50) NOT NULL,
	data_atualizacao datetime NOT NULL,
	usuario_atualizacao varchar(50) NOT NULL,
	ativo bit NOT NULL
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.fonte ADD CONSTRAINT
	PK_fonte PRIMARY KEY CLUSTERED 
	(
	id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
ALTER TABLE dbo.fonte ADD CONSTRAINT
	DF_fonte_ativo DEFAULT 1 FOR ativo
GO
ALTER TABLE dbo.fonte ADD CONSTRAINT
	FK_fonte_tipo_fonte FOREIGN KEY
	(
	id_tipo_fonte
	) REFERENCES dbo.tipo_fonte
	(
	id
	) ON UPDATE  NO ACTION 
	 ON DELETE  NO ACTION 
	
GO
	
INSERT INTO fonte (nome, url, id_tipo_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Diário Oficial dos municípios', 'http://diarioficialdosmunicipios.org', 1,  getdate(), 'sistema', getdate(), 'sistema', 1)
