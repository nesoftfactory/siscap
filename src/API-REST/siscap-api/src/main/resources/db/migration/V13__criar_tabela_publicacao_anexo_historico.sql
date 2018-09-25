CREATE TABLE [dbo].[publicacao_anexo_historico](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_publicacao_anexo] [int] NOT NULL,
	[mensagem] [varchar](100) NULL,
	[status] [varchar](50) NOT NULL check (status in ('Encontrado','Não Encontrado','Erro')),
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
 CONSTRAINT [PK_publicacao_anexo_historico] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[publicacao_anexo_historico]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_anexo_historico_publicacao_anexo] FOREIGN KEY([id_publicacao_anexo])
REFERENCES [dbo].[publicacao_anexo] ([id])
GO

ALTER TABLE [dbo].[publicacao_anexo_historico] CHECK CONSTRAINT [DF_publicacao_anexo_historico_publicacao_anexo]
GO

ALTER TABLE [dbo].[publicacao_anexo_historico]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_anexo_historico_usuario_criacao] FOREIGN KEY([usuario_criacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[publicacao_anexo_historico] CHECK CONSTRAINT [DF_publicacao_anexo_historico_usuario_criacao]
GO