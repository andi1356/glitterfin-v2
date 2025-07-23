openssl enc -d -aes-256-cbc -base64 -pbkdf2 -iter 1000 -salt -in .env.prod.enc -out .env.prod -pass "file:tools/enc_password_file"
