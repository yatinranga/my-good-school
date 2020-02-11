INSERT INTO oauth_client_details
	(client_id, resource_ids, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	("my-good-school","resource-server-api", "nxtlife", "read,write",
	"password,authorization_code", null, null, -1, -1, null, true);


