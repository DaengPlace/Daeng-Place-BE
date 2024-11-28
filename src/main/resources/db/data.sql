INSERT INTO member (
    email,
    name,
    nickname,
    profile_image_url,
    gender,
    birth_date,
    location_status
) VALUES
      ('test1@example.com', '홍길동', '댕댕이',  'http://example.com/profile1.jpg', 'MALE', '1990-01-01', false),
      ('test2@example.com', '김철수', '냥냥이',  'http://example.com/profile2.jpg', 'MALE', '1992-03-15', true),
      ('test3@example.com', '이영희', '멍뭉이',  'http://example.com/profile3.jpg', 'FEMALE', '1995-07-22', false),
      ('test4@example.com', '박민수', '도그맘',  'http://example.com/profile4.jpg', 'MALE', '1988-11-30', true),
      ('test5@example.com', '정미영', '캣대디',  'http://example.com/profile5.jpg', 'FEMALE', '1993-05-18', false),
      ('test6@example.com', '강지훈', '펫러버',  'http://example.com/profile6.jpg', 'MALE', '1991-09-25', true),
      ('test7@example.com', '조수민', '포메맘',  'http://example.com/profile7.jpg', 'FEMALE', '1994-12-08', false),
      ('test8@example.com', '윤승호', '댕댕이아빠',  'http://example.com/profile8.jpg', 'MALE', '1987-04-14', true),
      ('test9@example.com', '임하늘', '펫스타그램',  'http://example.com/profile9.jpg', 'FEMALE', '1996-02-28', false),
      ('test10@example.com', '신영준', '애견왕',  'http://example.com/profile10.jpg', 'MALE', '1989-08-11', true);

INSERT INTO address (road_address, detail_address, zipcode)
VALUES
    ('서울특별시 강남구 테헤란로 123', '4층', '06134'),
    ('부산광역시 해운대구 해운대로 456', NULL, '48094'),
    ('서울특별시 마포구 월드컵북로 235', '1층', '03922'),
    ('서울특별시 송파구 올림픽로 300', NULL, '05551'),
    ('경기도 고양시 일산동구 중앙로 1275', '2층', '10409'),
    ('서울특별시 강서구 공항대로 376', NULL, '07505'),
    ('서울특별시 용산구 이태원로 123', '3층', '04351'),
    ('경기도 성남시 분당구 판교로 255', NULL, '13486'),
    ('서울특별시 중구 을지로 100', '5층', '04538'),
    ('인천광역시 연수구 컨벤시아대로 153', '1층', '21998');

INSERT INTO place (name, description, category, is_parking, pet_fee, weight_limit, weather_type, address_id)
VALUES
    ('강남 애견 카페', '강남에 위치한 애견 친화 카페', 'RESTAURANT', TRUE, 5000, 10.5, 'INDOOR', 1),
    ('해운대 반려견 호텔', '해운대 근처의 반려동물 전용 호텔', 'HOTEL', TRUE, 20000, 20.0, 'OUTDOOR', 2),
    ('망원 애견 공원', '한강이 보이는 넓은 애견 공원', 'PARK', TRUE, 0, 30.0, 'OUTDOOR', 3),
    ('송파 24시 동물병원', '24시간 응급 진료가 가능한 동물병원', 'HOSPITAL', TRUE, 0, NULL, 'INDOOR', 4),
    ('일산 펫 호텔', '반려동물 전용 수영장이 있는 호텔', 'HOTEL', TRUE, 25000, 15.0, 'INDOOR', 5),
    ('강서 애견 카페', '넓은 공간의 애견 카페', 'RESTAURANT', TRUE, 8000, 12.0, 'INDOOR', 6),
    ('이태원 펫 다이닝', '반려동물과 함께하는 고급 레스토랑', 'RESTAURANT', TRUE, 10000, 8.0, 'INDOOR', 7),
    ('판교 동물병원', '최신 의료 시설을 갖춘 동물병원', 'HOSPITAL', TRUE, 0, NULL, 'INDOOR', 8),
    ('을지로 애견 카페', '도심 속 휴식 공간', 'RESTAURANT', FALSE, 6000, 10.0, 'INDOOR', 9),
    ('송도 반려견 놀이터', '실내 놀이터와 애견카페를 함께 운영', 'PARK', TRUE, 10000, 25.0, 'INDOOR', 10);