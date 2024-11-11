-- Prvo kreirajmo korisnike i lokacije ako već ne postoje
INSERT INTO users
    (id, email, username, password, full_name, address, is_active, followers_count, role, activated) VALUES
    (1, 'johndoe@example.com', 'johndoe', 'password123', 'John Doe', '123 Main St', true, 10, 'REGISTERED', true),
    (2, 'janedoe@example.com', 'janedoe', 'password456', 'Jane Doe', '456 Second St', true, 5, 'REGISTERED', true);

INSERT INTO locations (id, latitude, longitude, address) VALUES
    (1, 45.2671, 19.8335, '123 Main St'),
    (2, 44.7866, 20.4489, '456 Second St');

-- Sada unosimo postove
INSERT INTO posts (id, description, image_path, created_time, user_id, location_id) VALUES
    (1, 'Beautiful sunset at the beach', '/images/sunset.jpg', '2024-11-11 17:00:00', 1, 1),
    (2, 'Amazing view from the mountain top', '/images/mountain.jpg', '2024-11-12 09:30:00', 2, 2),
    (3, 'Exploring the old city streets', '/images/city.jpg', '2024-11-13 14:45:00', 1, 1);
INSERT INTO comments (id, text, created_time, user_id, post_id) VALUES
    (1, 'Amazing photo, really beautiful!', '2024-11-11 17:05:00', 2, 1), -- Jane Doe komentariše na post #1
    (2, 'Thanks! The view was breathtaking.', '2024-11-11 17:10:00', 1, 1), -- John Doe odgovara na komentar
    (3, 'Looks like a great place to visit!', '2024-11-12 10:00:00', 1, 2), -- John Doe komentariše na post #2
    (4, 'I love exploring old cities too!', '2024-11-13 15:00:00', 2, 3); -- Jane Doe komentariše na post #3