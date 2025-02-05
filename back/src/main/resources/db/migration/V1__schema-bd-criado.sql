CREATE TABLE
    estado (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(150) NOT NULL,
        uf VARCHAR(2) NOT NULL
    );

CREATE TABLE
    cidade (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(200) NOT NULL,
        id_estado INT NOT NULL,
        FOREIGN KEY (id_estado) REFERENCES estado (id)
    );

CREATE TABLE
    endereco (
        id SERIAL PRIMARY KEY,
        cep VARCHAR(9) NOT NULL,
        numero INT NOT NULL,
        id_cidade INT NOT NULL,
        FOREIGN KEY (id_cidade) REFERENCES cidade (id)
    );

CREATE TABLE
    usuario (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        sobrenome VARCHAR(200) NOT NULL,
        data_nascimento DATE NOT NULL,
        email VARCHAR(150) UNIQUE NOT NULL,
        whatsapp VARCHAR(12),
        id_endereco INT NOT NULL,
        senha VARCHAR(200) NOT NULL,
        advertencias INT,
        status VARCHAR(70) NOT NULL,
        FOREIGN KEY (id_endereco) REFERENCES endereco (id)
    );

CREATE TABLE
    editora (id SERIAL PRIMARY KEY, nome VARCHAR(150) NOT NULL);

CREATE TABLE
    idioma (id SERIAL PRIMARY KEY, nome VARCHAR(100) NOT NULL);

CREATE TABLE
    categoria (id SERIAL PRIMARY KEY, nome VARCHAR(150) NOT NULL);

CREATE TABLE
    autor (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        sobrenome VARCHAR(100) NOT NULL,
        nacionalidade VARCHAR(100) NOT NULL,
        nome_artistico VARCHAR(150)
    );

CREATE TABLE
    livro (
        id SERIAL PRIMARY KEY,
        titulo VARCHAR(150) NOT NULL,
        descricao VARCHAR(300) NOT NULL,
        data_publicacao DATE NOT NULL,
        capa BYTEA NOT NULL,
        status VARCHAR(100) NOT NULL,
        id_editora INT NOT NULL,
        id_idioma INT NOT NULL,
        id_dono INT NOT NULL,
        id_cidade INT NOT NULL,
        FOREIGN KEY (id_editora) REFERENCES editora (id),
        FOREIGN KEY (id_idioma) REFERENCES idioma (id),
        FOREIGN KEY (id_dono) REFERENCES usuario (id),
        FOREIGN KEY (id_cidade) REFERENCES cidade (id)
    );

CREATE TABLE
    livro_categoria (
        id SERIAL PRIMARY KEY,
        id_livro INT NOT NULL,
        id_categoria INT NOT NULL,
        FOREIGN KEY (id_livro) REFERENCES livro (id),
        FOREIGN KEY (id_categoria) REFERENCES categoria (id)
    );

CREATE TABLE
    livro_autor (
        id SERIAL PRIMARY KEY,
        id_livro INT NOT NULL,
        id_autor INT NOT NULL,
        FOREIGN KEY (id_livro) REFERENCES livro (id),
        FOREIGN KEY (id_autor) REFERENCES autor (id)
    );

CREATE TABLE
    solicitacoes (
        id SERIAL PRIMARY KEY,
        id_livro INT NOT NULL,
        id_usuario INT NOT NULL,
        status VARCHAR(100) NOT NULL,
        FOREIGN KEY (id_livro) REFERENCES livro (id),
        FOREIGN KEY (id_usuario) REFERENCES usuario (id)
    );