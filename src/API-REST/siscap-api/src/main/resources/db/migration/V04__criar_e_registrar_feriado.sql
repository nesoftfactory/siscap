CREATE TABLE dbo.feriado
	(
	id int NOT NULL IDENTITY (1, 1),
	nome varchar(50) NOT NULL,
	data date NOT NULL,
	id_fonte int NULL,
	data_criacao datetime NOT NULL,
	usuario_criacao varchar(50) NOT NULL,
	data_atualizacao datetime NOT NULL,
	usuario_atualizacao varchar(50) NOT NULL,
	ativo bit NOT NULL
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.feriado ADD CONSTRAINT
	PK_feriado PRIMARY KEY CLUSTERED 
	(
	id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
ALTER TABLE dbo.feriado ADD CONSTRAINT
	DF_feriado_ativo DEFAULT 1 FOR ativo
GO
ALTER TABLE dbo.feriado ADD CONSTRAINT
	FK_feriado_fonte FOREIGN KEY
	(
	id_fonte
	) REFERENCES dbo.fonte
	(
	id
	) ON UPDATE  NO ACTION 
	 ON DELETE  NO ACTION 
	
GO

set language brazilian	
INSERT INTO feriado (nome, data, id_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Independência do Brasil', '07/09/2018', null, getdate(), 'sistema', getdate(), 'sistema', 1)
INSERT INTO feriado (nome, data, id_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Natal', '25/12/2018', null, getdate(), 'sistema', getdate(), 'sistema', 1)
