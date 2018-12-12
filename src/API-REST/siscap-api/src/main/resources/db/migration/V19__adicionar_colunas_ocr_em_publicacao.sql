ALTER TABLE dbo.publicacao ADD situacao varchar(30) NULL;
ALTER TABLE dbo.publicacao ADD quantidade_tentativas_ocr int NULL;
ALTER TABLE dbo.publicacao ADD quantidade_tentativas_indexacao int NULL;