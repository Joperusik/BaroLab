CREATE TABLE mod_guides
(
    id UUID PRIMARY KEY,
    mod_external_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_mod_guides_mod_post
        FOREIGN KEY (mod_external_id)
            REFERENCES mod_posts (external_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_mod_guides_author
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);
