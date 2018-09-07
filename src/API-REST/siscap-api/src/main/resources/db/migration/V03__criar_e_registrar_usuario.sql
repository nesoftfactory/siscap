CREATE TABLE dbo.usuario
	(
	id int NOT NULL IDENTITY (1, 1),
	nome varchar(50) NOT NULL,
	cpf varchar(11) NOT NULL,
	login varchar(50) NOT NULL,
	admin bit NOT NULL,
	data_criacao datetime NOT NULL,
	usuario_criacao varchar(50) NOT NULL,
	data_atualizacao datetime NOT NULL,
	usuario_atualizacao varchar(50) NOT NULL,
	ativo bit NOT NULL
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.usuario ADD CONSTRAINT
	PK_usuario PRIMARY KEY CLUSTERED 
	(
	id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
ALTER TABLE dbo.usuario ADD CONSTRAINT
	DF_usuario_admin DEFAULT 0 FOR admin
GO
ALTER TABLE dbo.usuario ADD CONSTRAINT
	DF_usuario_ativo DEFAULT 1 FOR ativo
GO
	
INSERT INTO usuario (nome, cpf, login, admin, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Administrador', '11111111111', 'admin', 1,  getdate(), 'sistema', getdate(), 'sistema', 1)
