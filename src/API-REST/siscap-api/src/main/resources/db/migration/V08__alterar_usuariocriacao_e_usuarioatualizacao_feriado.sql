DELETE FROM feriado
GO

ALTER TABLE dbo.feriado ALTER COLUMN usuario_criacao int;

ALTER TABLE dbo.feriado ALTER COLUMN usuario_atualizacao int;

ALTER TABLE dbo.feriado ADD CONSTRAINT
	DF_feriado_usuario_criacao FOREIGN KEY (usuario_criacao) REFERENCES dbo.usuario(id)
GO
ALTER TABLE dbo.feriado ADD CONSTRAINT
	DF_feriado_usuario_atualizacao FOREIGN KEY (usuario_atualizacao) REFERENCES dbo.usuario(id)
GO

set language brazilian	
declare @id_usuario int
select top 1 @id_usuario = id from usuario

INSERT INTO feriado (nome, data, id_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Independência do Brasil', '07/09/2018', null, getdate(), @id_usuario, getdate(), @id_usuario, 1)
INSERT INTO feriado (nome, data, id_fonte, data_criacao, usuario_criacao, data_atualizacao, usuario_atualizacao, ativo) 
	VALUES('Natal', '25/12/2018', null, getdate(), @id_usuario, getdate(), @id_usuario, 1)
	
