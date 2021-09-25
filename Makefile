SERVICE := emailer

app-build:
	docker-compose build

app-run:
	docker run -d \
		-p 8080:8080 \
		-e MAIL_GUN_URL=https://api.mailgun.net/v3/sandbox8a16e779365f41e9b0c38839c4cc873c.mailgun.org/messages \
		-e MAIL_GUN_USER=api \
		-e MAIL_GUN_PASSWORD=c8c4b545fa6b027e523699ee4a4d350a-45f7aa85-819ca8a3 \
		--network tim-net \
		--name $(SERVICE) \
		tim/$(SERVICE)

app-cleanup:
	docker stop $(SERVICE)
	docker rm $(SERVICE)