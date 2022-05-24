insert into person(created_at, login)
values (now(), 'test');

insert into account(created_at, number, pin_code, balance, person_id)
values (now(), '0000000000000000', '$2a$10$8v00PoWg8haz6C1GkejboOiW3Sgo7UAEApE0zrzIkBDvRBwe5ycQm', 0.00, 1),
       (now(), '0000000000000001', '$2a$10$8v00PoWg8haz6C1GkejboOiW3Sgo7UAEApE0zrzIkBDvRBwe5ycQm', 10.00, 1);

insert into operation(created_at, type, amount, source_id, dest_id)
values (now(), 'DEPOSIT', 15.00, null, 1),
       (now(), 'DEPOSIT', 20.00, null, 2),
       (now(), 'TRANSFER', 15.00, 1, 2),
       (now(), 'WITHDRAW', 25.00, 2, null);
