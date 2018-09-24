CREATE TABLE [dbo].[publicacao_historico](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_publicacao] [int] NOT NULL,
	[mensagem] [varchar](20) NULL,
	[sucesso] [bit] NOT NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
 CONSTRAINT [PK_publicacao_historico] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[publicacao_historico] ADD  CONSTRAINT [DF_publicacao_historico_sucesso]  DEFAULT ((0)) FOR [sucesso]
GO

ALTER TABLE [dbo].[publicacao_historico]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_historico_publicacao] FOREIGN KEY([id_publicacao])
REFERENCES [dbo].[publicacao] ([id])
GO

ALTER TABLE [dbo].[publicacao_historico] CHECK CONSTRAINT [DF_publicacao_historico_publicacao]
GO

ALTER TABLE [dbo].[publicacao_historico]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_historico_usuario_criacao] FOREIGN KEY([usuario_criacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[publicacao_historico] CHECK CONSTRAINT [DF_publicacao_historico_usuario_criacao]
GO