DROP TABLE IF EXISTS shortener_details;

CREATE TABLE shortener_details (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  client_name VARCHAR(100) NOT NULL,
  original_url VARCHAR(500) NOT NULL,
  shortened_url VARCHAR(200) DEFAULT NULL,
  days_to_persist VARCHAR(2) DEFAULT NULL,
  created_date timestamp DEFAULT NULL
);

/*INSERT INTO shortener_details (client_name, original_url, shortened_url) VALUES
  ('divya-client1', 'http://localhost:9090/abcd', 'shortUrl1'),
  ('divya-client2', 'http://localhost:9090/abcd2', 'shortUrl2');*/