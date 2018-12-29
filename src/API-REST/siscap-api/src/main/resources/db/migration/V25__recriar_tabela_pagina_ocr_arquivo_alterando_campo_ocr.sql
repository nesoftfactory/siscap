DROP TABLE IF EXISTS pagina_ocr_arquivo
GO

CREATE TABLE [dbo].[pagina_ocr_arquivo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[ocr] [varchar](MAX) NOT NULL,
	[id_arquivo] [int] NOT NULL,
	[pagina] [int] NOT NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
	[data_atualizacao] [datetime] NOT NULL,
	[usuario_atualizacao] [int] NULL,
 CONSTRAINT [PK_pagina_ocr_arquivo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO