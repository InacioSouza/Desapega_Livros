ALTER TABLE livro ADD COLUMN subtitulo VARCHAR(350);
ALTER TABLE livro DROP COLUMN data_publicacao;
ALTER TABLE livro ADD COLUMN ano_publicacao INT;