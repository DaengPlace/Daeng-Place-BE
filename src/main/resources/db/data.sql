INSERT INTO member (
    email,
    name,
    nickname,
    phone,
    profile_image_url,
    gender,
    birth_date,
    location_status
) VALUES (
             'test@example.com',
             '홍길동',
             '댕댕이',
             '010-1234-5678',
             'http://example.com/profile.jpg',
             'MALE',
             '1990-01-01',
             false
         );
INSERT INTO address (road_address, detail_address, zipcode)
VALUES
    ('서울특별시 강남구 테헤란로 123', '4층', '06134'),
    ('부산광역시 해운대구 해운대로 456', NULL, '48094');
INSERT INTO place (name, description, category, is_parking, pet_fee, weight_limit, weather_type, address_id)
VALUES
    ('강남 애견 카페', '강남에 위치한 애견 친화 카페', 'RESTAURANT', TRUE, 5000, 10.5, 'INDOOR', 1),
    ('해운대 반려견 호텔', '해운대 근처의 반려동물 전용 호텔', 'HOTEL', TRUE, 20000, 20.0, 'OUTDOOR', 2);