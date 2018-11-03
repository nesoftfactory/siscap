BEGIN TRANSACTION
GO

DROP TABLE IF EXISTS notificacao_usuarios
GO

DROP TABLE IF EXISTS notificacao
GO

CREATE TABLE [dbo].[notificacao](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_publicacao] int NOT NULL,
	[tipo] [varchar](30) NOT NULL,
 	[texto] [varchar](8000) NOT NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
	[data_atualizacao] [datetime] NOT NULL,
	[usuario_atualizacao] [int] NULL,
 CONSTRAINT [PK_notificacao] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[notificacao]  WITH CHECK ADD  CONSTRAINT [FK_notificacao_publicacao] FOREIGN KEY([id_publicacao])
REFERENCES [dbo].[publicacao] ([id])
GO

ALTER TABLE [dbo].[notificacao] CHECK CONSTRAINT [FK_notificacao_publicacao]
GO

ALTER TABLE [dbo].[notificacao]  WITH CHECK ADD  CONSTRAINT [DF_notificacao_usuario_atualizacao] FOREIGN KEY([usuario_atualizacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[notificacao] CHECK CONSTRAINT [DF_notificacao_usuario_atualizacao]
GO

ALTER TABLE [dbo].[notificacao]  WITH CHECK ADD  CONSTRAINT [DF_notificacao_usuario_criacao] FOREIGN KEY([usuario_criacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[notificacao] CHECK CONSTRAINT [DF_notificacao_usuario_criacao]
GO

-- Criar tabela de relacionamento
CREATE TABLE [dbo].[notificacao_usuarios](
	[id_notificacao] [int] NOT NULL,
	[id_usuario] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[notificacao_usuarios]  WITH CHECK ADD  CONSTRAINT [FK_notificacao_usuarios_notificacao] FOREIGN KEY([id_notificacao])
REFERENCES [dbo].[notificacao] ([id])
GO

ALTER TABLE [dbo].[notificacao_usuarios] CHECK CONSTRAINT [FK_notificacao_usuarios_notificacao]
GO

ALTER TABLE [dbo].[notificacao_usuarios]  WITH CHECK ADD  CONSTRAINT [FK_notificacao_usuarios_usuario] FOREIGN KEY([id_usuario])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[notificacao_usuarios] CHECK CONSTRAINT [FK_notificacao_usuarios_usuario]
GO

-- Povoar tabela

set language brazilian	
DECLARE @id_notificacao int, @id_usuario int, @id_publicacao int
SELECT TOP 1 @id_usuario = id FROM usuario
SELECT TOP 1 @id_publicacao = id FROM publicacao

INSERT INTO notificacao (id_publicacao, tipo, texto, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao) 
	VALUES(@id_publicacao, 'CAPTURA', 'Teste criado no ato da criação', getdate(), @id_usuario, getdate(), @id_usuario)
SELECT @id_notificacao = @@IDENTITY

INSERT INTO notificacao_usuarios (id_notificacao, id_usuario) VALUES(@id_notificacao, @id_usuario)
GO
COMMIT
