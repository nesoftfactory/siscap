BEGIN TRANSACTION
GO

DROP TABLE IF EXISTS notificacao_config_usuarios
GO

DROP TABLE IF EXISTS notificacao_config
GO

CREATE TABLE [dbo].[notificacao_config](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[tipo] [varchar](30) NOT NULL,
	[ativo] [bit] NOT NULL,
 	[qt_tentativas] [int] NOT NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
	[data_atualizacao] [datetime] NOT NULL,
	[usuario_atualizacao] [int] NULL,
 CONSTRAINT [PK_notificacao_config] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[notificacao_config] ADD  CONSTRAINT [DF_notificacao_config_ativo]  DEFAULT ((1)) FOR [ativo]
GO

ALTER TABLE [dbo].[notificacao_config]  WITH CHECK ADD  CONSTRAINT [DF_notificacao_config_usuario_atualizacao] FOREIGN KEY([usuario_atualizacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[notificacao_config] CHECK CONSTRAINT [DF_notificacao_config_usuario_atualizacao]
GO

ALTER TABLE [dbo].[notificacao_config]  WITH CHECK ADD  CONSTRAINT [DF_notificacao_config_usuario_criacao] FOREIGN KEY([usuario_criacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[notificacao_config] CHECK CONSTRAINT [DF_notificacao_config_usuario_criacao]
GO

-- Criar tabela de relacionamento
CREATE TABLE [dbo].[notificacao_config_usuarios](
	[id_notificacao_config] [int] NOT NULL,
	[id_usuario] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[notificacao_config_usuarios]  WITH CHECK ADD  CONSTRAINT [FK_notificacao_config_usuarios_notificacao_config] FOREIGN KEY([id_notificacao_config])
REFERENCES [dbo].[notificacao_config] ([id])
GO

ALTER TABLE [dbo].[notificacao_config_usuarios] CHECK CONSTRAINT [FK_notificacao_config_usuarios_notificacao_config]
GO

ALTER TABLE [dbo].[notificacao_config_usuarios]  WITH CHECK ADD  CONSTRAINT [FK_notificacao_config_usuarios_usuario] FOREIGN KEY([id_usuario])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[notificacao_config_usuarios] CHECK CONSTRAINT [FK_notificacao_config_usuarios_usuario]
GO

-- Povoar tabela

set language brazilian	
DECLARE @id_notificacao_config int, @id_usuario int
SELECT TOP 1 @id_usuario = id FROM usuario

INSERT INTO notificacao_config (tipo, qt_tentativas, ativo, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao) 
	VALUES('CAPTURA', 3, 1, getdate(), @id_usuario, getdate(), @id_usuario)
SELECT @id_notificacao_config = @@IDENTITY

INSERT INTO notificacao_config_usuarios (id_notificacao_config, id_usuario) VALUES(@id_notificacao_config, @id_usuario)
GO
COMMIT
