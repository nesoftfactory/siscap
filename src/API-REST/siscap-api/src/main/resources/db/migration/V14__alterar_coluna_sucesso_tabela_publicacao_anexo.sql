delete from [dbo].[publicacao_anexo_historico];
drop table [dbo].[publicacao_anexo_historico];

delete from [dbo].[publicacao_anexo];
drop table [dbo].[publicacao_anexo];

CREATE TABLE [dbo].[publicacao_anexo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[nome] [varchar](50) NULL,
	[id_publicacao] [int] NOT NULL,		
	[id_arquivo] [int] NULL,
	[sucesso] [bit] NOT NULL,	
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
	[data_atualizacao] [datetime] NOT NULL,
	[usuario_atualizacao] [int] NULL,
 CONSTRAINT [PK_publicacao_anexo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


ALTER TABLE [dbo].[publicacao_anexo]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_anexo_publicacao] FOREIGN KEY([id_publicacao])
REFERENCES [dbo].[publicacao] ([id])
GO

ALTER TABLE [dbo].[publicacao_anexo] ADD  CONSTRAINT [DF_publicacao_anexo_sucesso]  DEFAULT ((0)) FOR [sucesso]
GO

ALTER TABLE [dbo].[publicacao_anexo] CHECK CONSTRAINT [DF_publicacao_anexo_publicacao]
GO

ALTER TABLE [dbo].[publicacao_anexo]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_anexo_arquivo] FOREIGN KEY([id_arquivo])
REFERENCES [dbo].[arquivo] ([id])
GO

ALTER TABLE [dbo].[publicacao_anexo] CHECK CONSTRAINT [DF_publicacao_anexo_arquivo]
GO

ALTER TABLE [dbo].[publicacao_anexo]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_anexo_usuario_atualizacao] FOREIGN KEY([usuario_atualizacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[publicacao_anexo] CHECK CONSTRAINT [DF_publicacao_anexo_usuario_atualizacao]
GO

ALTER TABLE [dbo].[publicacao_anexo]  WITH CHECK ADD  CONSTRAINT [DF_publicacao_anexo_usuario_criacao] FOREIGN KEY([usuario_criacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[publicacao_anexo] CHECK CONSTRAINT [DF_publicacao_anexo_usuario_criacao]
GO



CREATE TABLE [dbo].[publicacao_anexo_historico](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_publicacao_anexo] [int] NOT NULL,
	[mensagem] [varchar](100) NULL,
	[sucesso] [bit] NOT NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
 CONSTRAINT [PK_publicacao_anexo_historico] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[publicacao_anexo_historico] ADD  CONSTRAINT [DF_publicacao_anexo_historico_sucesso]  DEFAULT ((0)) FOR [sucesso]
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


