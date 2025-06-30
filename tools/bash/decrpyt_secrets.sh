pwd
openssl enc -d -aes-256-cbc -pbkdf2 -iter 1000 -salt -in secrets.enc -out secrets -pass "file:tools/enc_password_file"