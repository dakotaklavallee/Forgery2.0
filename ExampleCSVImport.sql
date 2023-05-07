COPY combined_members(member_email, months_subscribed)
FROM 'C:\sampledb\TribesAndPatreonUsers.csv'
DELIMITER ','
CSV HEADER;

COPY patreon_members(member_email, months_subscribed)
FROM 'C:\sampledb\PatreonUserData.csv'
DELIMITER ','
CSV HEADER;

COPY tribes_members(member_email, months_subscribed)
FROM 'C:\sampledb\TribesUserData.csv'
DELIMITER ','
CSV HEADER;