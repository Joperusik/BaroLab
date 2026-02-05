-- 1. Таблица Users
CREATE TABLE users
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    login      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL, -- Храните здесь хэш, а не чистый пароль
    status     VARCHAR(50)  NOT NULL    DEFAULT 'active',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Таблица Admins (Отдельная сущность, как в запросе)
CREATE TABLE admins
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    login      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL, -- Например: 'superadmin', 'moderator'
    status     VARCHAR(50)  NOT NULL    DEFAULT 'active',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Таблица Posts
-- Связь: Один User -> Много Post
CREATE TABLE posts
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL,
    rating     INTEGER                  DEFAULT 0,
    status     VARCHAR(50) NOT NULL     DEFAULT 'published', -- Например: 'draft', 'published', 'archived'
    content    TEXT,                                         -- Добавил поле контента, так как пустой пост не имеет смысла
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Внешний ключ на пользователя
    CONSTRAINT fk_post_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

-- 4. Таблица Comments
-- Связь: Один Post -> Много Comments
-- Связь: Один User -> Много Comments
CREATE TABLE comments
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    post_id    UUID NOT NULL,
    user_id    UUID NOT NULL,
    body       TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Внешний ключ на пост
    CONSTRAINT fk_comment_post
        FOREIGN KEY (post_id)
            REFERENCES posts (id)
            ON DELETE CASCADE,

    -- Внешний ключ на пользователя (автора комментария)
    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);