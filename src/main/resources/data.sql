-- Insert semesters with specific UUIDs
INSERT IGNORE INTO semester (id, name) VALUES
    ('a1b2c3d4-e5f6-4789-abc1-234567890abc', 'Semester 1'),
    ('a1b2c3d4-e5f6-4789-abc2-234567890abc', 'Semester 2'),
    ('a1b2c3d4-e5f6-4789-abc3-234567890abc', 'Semester 3');

-- Insert weeks with specific UUIDs
INSERT IGNORE INTO week (id, name) VALUES
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h0i', 'week 1'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h1i', 'week 2'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h2i', 'week 3'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h3i', 'week 4'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h4i', 'week 5'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h5i', 'week 6'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h6i', 'week 7'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h7i', 'week 8'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h8i', 'week 9'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9h9i', 'week 10'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9ha0', 'week 11'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9ha1', 'week 12'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9ha2', 'week 13'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9ha3', 'week 14'),
    ('e5f6e7f8-9a0b-4d1a-b3c4-5d6e7f8g9ha4', 'week 15');

-- Insert sessions with specific UUIDs
INSERT IGNORE INTO `session` (id, name) VALUES
    ('f6e7f8g9-0b1a-2c3d-4e5f-6a7b8c9d0e1f', 'Morning'),
    ('f6e7f8g9-0b1a-2c3d-4e5f-6a7b8c9d0e2f', 'Afternoon'),
    ('f6e7f8g9-0b1a-2c3d-4e5f-6a7b8c9d0e3f', 'Evening');

-- Insert laboratories with specific IDs and capacities
INSERT IGNORE INTO laboratory (id, capacity) VALUES
    ('P101', 40),
    ('P102', 40),
    ('P103', 40),
    ('P104', 40),
    ('P105', 40),
    ('P106', 40),
    ('P107', 40),
    ('P108', 40),
    ('P109', 40),
    ('P110', 40),
    ('P111', 40),
    ('P201', 40),
    ('P202', 40),
    ('P203', 40),
    ('P204', 40),
    ('P205', 40),
    ('P206', 40),
    ('P207', 40),
    ('P208', 40),
    ('P209', 40),
    ('P210', 40),
    ('P211', 40),
    ('P01/NĐH - Lầu 7', 40),
    ('P02/NĐH - Lầu 7', 40),
    ('P03/NĐH - Lầu 7', 40),
    ('P06/NĐH - Lầu 7', 40);

-- Insert school_year with specific UUIDs
INSERT IGNORE INTO school_year (id, name) VALUES
    ('d0f1e2a3-b4c5-6789-a1b2-234567890abc', '2023-2024'),
    ('d0f1e2a3-b4c5-6789-a1b3-234567890abc', '2024-2025');

-- Insert TimeSets with specific UUIDs for Semester 1 and SchoolYear 2023-2024
-- Ensure the Semester and School Year data exists in your tables

INSERT IGNORE INTO time_set (id, start_date, end_date, is_current, semester, school_year, week)
VALUES
    ('9b8b0d5e-883e-4f96-91ed-f202f85f00b0', '2024-12-02', '2024-12-08', 1,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 1' LIMIT 1)
    ),
    ('63c7c1c4-660b-4905-9c3d-9c46a40d51a1', '2024-12-09', '2024-12-15', 2,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 2' LIMIT 1)
    ),
    ('0e924b75-3f1c-43a4-92d1-4ea17306c724', '2024-12-16', '2024-12-22', 3,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 3' LIMIT 1)
    ),
    ('6e0b3424-4db1-4c65-97a4-10e04c4c7b58', '2024-12-23', '2024-12-29', 4,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 4' LIMIT 1)
    ),
    ('03a2d340-3b3c-4b9b-8487-d9738d69de90', '2024-12-30', '2025-01-05', 5,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 5' LIMIT 1)
    ),
    ('7672c1a2-b37f-4df0-b86a-d85a93d3fe4a', '2025-01-06', '2025-01-12', 6,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 6' LIMIT 1)
    ),
    ('2d9b4311-c65f-4745-8a76-7f9b22f88c72', '2025-01-13', '2025-01-19', 7,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 7' LIMIT 1)
    ),
    ('1b3929f4-b876-42b7-bca7-61a415a6b726', '2025-01-20', '2025-01-26', 8,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 8' LIMIT 1)
    ),
    ('58b3a0f4-5f64-4c35-88b7-8d27135de015', '2025-01-27', '2025-02-02', 9,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 9' LIMIT 1)
    ),
    ('42f70f8f-8e9b-4d6a-a830-7a68b289f381', '2025-02-03', '2025-02-09', 10,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 10' LIMIT 1)
    ),
    ('12a8f536-d35f-4cc1-befb-67f906d1c809', '2025-02-10', '2025-02-16', 11,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 11' LIMIT 1)
    ),
    ('08cc9b8d-d1d7-4ff3-b7a4-5c060cf0e25d', '2025-02-17', '2025-02-23', 12,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 12' LIMIT 1)
    ),
    ('5a54e0cd-99b8-4c43-a08f-2ec50f44a5f3', '2025-02-24', '2025-03-02', 13,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 13' LIMIT 1)
    ),
    ('98e64f34-e50f-46a6-98f9-c987c1a82962', '2025-03-03', '2025-03-09', 14,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 14' LIMIT 1)
    ),
    ('153b43f1-d0d1-485b-a457-df0327b0f03e', '2025-03-10', '2025-03-16', 15,
        (SELECT id FROM semester WHERE name = 'Semester 1' LIMIT 1),
        (SELECT id FROM school_year WHERE name = '2023-2024' LIMIT 1),
        (SELECT id FROM week WHERE name = 'week 15' LIMIT 1)
    );
