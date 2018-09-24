CREATE TABLE [dbo].[publicacao](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[nome] [varchar](50) NULL,
	[id_fonte] [int] NOT NULL,
	[data] [date] NOT NULL,
	[codigo] [varchar](20) NULL,
	[id_arquivo] [int] NULL,
	[sucesso] [bit] NOT NULL,
	[possui_anexo] [bit] NOT NULL,
	[quatidade_tentativas] [int] NOT NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
	[data_atualizacao] [datetime] NOT NULL,
	[usuario_atualizacao] [int] NULL,
 CONSTRAINT [PK_publicacao] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[publicacao] ADD  CONSTRAINT [DF_publicacao_sucesso]  DEFAULT ((0)) FOR [sucesso]
GO

ALTER TABLE [dbo].[publicacao] ADD  CONSTRAINT [DF_publicacao_possui_anexo]  DEFAULT ((0)) FOR [possui_anexo]
GO

ALTER TABLE [dbo].[publicacao]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_fonte] FOREIGN KEY([id_fonte])
REFERENCES [dbo].[fonte] ([id])
GO

ALTER TABLE [dbo].[publicacao] CHECK CONSTRAINT [DF_publicacao_fonte]
GO

ALTER TABLE [dbo].[publicacao]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_arquivo] FOREIGN KEY([id_arquivo])
REFERENCES [dbo].[arquivo] ([id])
GO

ALTER TABLE [dbo].[publicacao] CHECK CONSTRAINT [DF_publicacao_arquivo]
GO

ALTER TABLE [dbo].[publicacao]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_usuario_atualizacao] FOREIGN KEY([usuario_atualizacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[publicacao] CHECK CONSTRAINT [DF_publicacao_usuario_atualizacao]
GO

ALTER TABLE [dbo].[publicacao]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_usuario_criacao] FOREIGN KEY([usuario_criacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[publicacao] CHECK CONSTRAINT [DF_publicacao_usuario_criacao]
GO
