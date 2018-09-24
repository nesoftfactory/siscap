CREATE TABLE [dbo].[arquivo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[nome] [varchar](100) NULL,
	[tamanho] [int] NOT NULL,
	[tipo] [varchar](50) NULL,
	[link] [varchar](100) NULL,
	[conteudo] [varbinary](max) NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
	[data_atualizacao] [datetime] NOT NULL,
	[usuario_atualizacao] [int] NULL,
 CONSTRAINT [PK_arquivo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO