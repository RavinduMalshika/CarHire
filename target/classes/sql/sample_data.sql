insert into User values
                     ("Supun", "Supun Fernando", "supun@email.com", "0761234567", md5("supun")),
                     ("Nipun", "Nipun Perera", "nipun@email.com", "0771234567", md5("nipun")),
                     ("Kasun", "Kasun Jayasekara", "kasun@email.com", "0711234567", md5("kasun")),
                     ("Nadun", "Nadun Rodrigo", "nadun@email.com", "0701234567", md5("nadun"));

insert into Category values
                         ("Sedan", "Supun", null),
                         ("SUV", "Nadun", "Kasun"),
                         ("Coupe", "Nipun", null),
                         ("Hatchback", "Supun", null);

insert into Brand values
                      ("Toyota", "Nipun", null),
                      ("Honda", "Kasun", null),
                      ("BMW", "Nadun", null),
                      ("Mazda", "Nipun", null);

insert into Model values
                      ("Allion", "Toyota", "Sedan", "Supun", null),
                      ("Premio", "Toyota", "Sedan", "Kasun", null),
                      ("Fit", "Honda", "Hatchback", "Nadun", null),
                      ("Mazda 3", "Mazda", "Sedan", "Nipun", null),
                      ("Pruis", "Toyota", "Hatchback", "Supun", "Nipun");