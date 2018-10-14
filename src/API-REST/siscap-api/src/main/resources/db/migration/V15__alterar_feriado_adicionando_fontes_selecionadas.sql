BEGIN TRANSACTION
GO

DROP TABLE IF EXISTS feriado_fonte
GO

DROP TABLE IF EXISTS feriado
GO

CREATE TABLE [dbo].[feriado](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[nome] [varchar](50) NOT NULL,
	[data] [date] NOT NULL,
	[ativo] [bit] NOT NULL,
	[fixo] [bit] NOT NULL,
	[todas_fontes] [bit] NOT NULL,
	[data_criacao] [datetime] NOT NULL,
	[usuario_criacao] [int] NULL,
	[data_atualizacao] [datetime] NOT NULL,
	[usuario_atualizacao] [int] NULL,
 CONSTRAINT [PK_feriado] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[feriado] ADD  CONSTRAINT [DF_feriado_ativo]  DEFAULT ((1)) FOR [ativo]
GO

ALTER TABLE [dbo].[feriado] ADD  CONSTRAINT [DF_feriado_fixo]  DEFAULT ((0)) FOR [fixo]
GO

ALTER TABLE [dbo].[feriado] ADD  CONSTRAINT [DF_feriado_todas_fontes]  DEFAULT ((0)) FOR [todas_fontes]
GO

ALTER TABLE [dbo].[feriado]  WITH CHECK ADD  CONSTRAINT [DF_feriado_usuario_atualizacao] FOREIGN KEY([usuario_atualizacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[feriado] CHECK CONSTRAINT [DF_feriado_usuario_atualizacao]
GO

ALTER TABLE [dbo].[feriado]  WITH CHECK ADD  CONSTRAINT [DF_feriado_usuario_criacao] FOREIGN KEY([usuario_criacao])
REFERENCES [dbo].[usuario] ([id])
GO

ALTER TABLE [dbo].[feriado] CHECK CONSTRAINT [DF_feriado_usuario_criacao]
GO

-- Criar tabela de relacionamento
CREATE TABLE [dbo].[feriado_fonte](
	[id_feriado] [int] NOT NULL,
	[id_fonte] [int] NOT NULL
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[feriado_fonte]  WITH CHECK ADD  CONSTRAINT [FK_feriado_fonte_feriado] FOREIGN KEY([id_feriado])
REFERENCES [dbo].[feriado] ([id])
GO

ALTER TABLE [dbo].[feriado_fonte] CHECK CONSTRAINT [FK_feriado_fonte_feriado]
GO

ALTER TABLE [dbo].[feriado_fonte]  WITH CHECK ADD  CONSTRAINT [FK_feriado_fonte_fonte] FOREIGN KEY([id_fonte])
REFERENCES [dbo].[fonte] ([id])
GO

ALTER TABLE [dbo].[feriado_fonte] CHECK CONSTRAINT [FK_feriado_fonte_fonte]
GO

-- Povoar tabela

set language brazilian	
DECLARE @id_fonte int, @id_feriado int, @id_usuario int
SELECT TOP 1 @id_fonte = id FROM fonte
SELECT TOP 1 @id_usuario = id FROM usuario

INSERT INTO feriado (nome, data, ativo, fixo, todas_fontes, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao) 
	VALUES('Independência do Brasil', '07/09/2018', 1, 1, 1, getdate(), @id_usuario, getdate(), @id_usuario)
INSERT INTO feriado (nome, data, ativo, fixo, todas_fontes, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao) 
	VALUES('Natal', '25/12/2018', 1, 1, 1, getdate(), @id_usuario, getdate(), @id_usuario)

INSERT INTO feriado (nome, data, ativo, fixo, todas_fontes, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao) 
	VALUES('Somente para fonte ' + LTRIM(RTRIM(cast(@id_fonte as varchar(3)))), '19/10/2018', 1, 0, 0, getdate(), @id_usuario, getdate(), @id_usuario)
SELECT @id_feriado = @@IDENTITY

INSERT INTO feriado_fonte (id_feriado, id_fonte) VALUES(@id_feriado, @id_fonte)
GO
COMMIT
