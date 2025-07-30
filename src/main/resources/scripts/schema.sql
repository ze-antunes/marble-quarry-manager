-- Criação da tabela blocks (blocos de mármore)
CREATE TABLE blocks (
    id SERIAL PRIMARY KEY,
    marble_type VARCHAR(100) NOT NULL,
    length_m DOUBLE PRECISION NOT NULL,
    height_m DOUBLE PRECISION NOT NULL,
    thickness_m DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('available', 'reserved'))
);

-- Criação da tabela orders (encomendas)
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    unit_count INT NOT NULL,
    unit_length_m DOUBLE PRECISION NOT NULL,
    unit_height_m DOUBLE PRECISION NOT NULL,
    unit_thickness_m DOUBLE PRECISION NOT NULL
);

-- Criação da tabela order_blocks (blocos reservados para uma encomenda)
CREATE TABLE order_blocks (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    block_id INT NOT NULL REFERENCES blocks(id) ON DELETE CASCADE,
    UNIQUE(order_id, block_id)
);
