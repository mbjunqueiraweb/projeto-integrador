## Projeto Integrador

Implemented
### US1
- (Implementar o URI para devolver na response)


### US2
- Put de order
- Tratamento de erro em caso de produto sem estoque

### US3
 - Get /list
 - Verifique a localização de um produto por armazém.
 
 ### US4
  - Get /loc
  - Consultar o estoque de um produto em todos os armazéns.
  
  ### US5
   - Consultar a validade por lote.
   - Get verificando validade
   - Get verificando categoria e dias a vencer de um produto.
 
 Todo list


### Revisão final
- Refatorar Classes separando Entities de DTOs
- (Testes)
- (Documentação )
- (Readme.md)
- (Incluir o modelo)

Modelo de SQL

```

##User.java
INSERT INTO users (username, password, enabled, profile_type) values ("Matheus", "AJDUJ*&@!", true, "AGENT");
INSERT INTO users (username, password, enabled, profile_type) values ("Mauro", "Alkvme*&@!", false, "BUYER");
##Warehouse.java
INSERT INTO warehouses (name) values ("warehouse1");
INSERT INTO warehouses (name) values ("warehouse2");
INSERT INTO warehouses (name) values ("warehouse3");
##Seller.java
INSERT INTO sellers (name) values ("seller1"),("seller2"), ("seller3");
##Section.java
INSERT INTO sections (type, total_space, warehouse_id) values (0, 1000, 1),(1, 2000, 2), (1, 2000, 3);
##ProductAnnoucement.java
insert into products_announcements (brand, maximum_temperature, minimum_temperature, name, price, volume, product_id, seller_id) values
("sadia", 10, 5, "presunto sadia",15,0.5,1, 1),
("três marias", 10, 5, "queijo 3 marias",20,0.4,2, 2),
("turma da mônica", 15, 10, "maçã turma da mônica",10,1,3, 3),
("horta", 15, 10, "alface da horta",5,1,4, 4),
("perdigão", 0, -5, "frango perdigão",25,2,5, 5);
##Products.java
insert into products (name, product_type) values ("presunto", 1),("queijo",1),("maçã", 0),("alface",0),("frango", 2),("batata",2);
##Buyer.java
INSERT INTO buyers (name) values ("fulano"), ("beltrano"), ("ciclano");
##Cart.java
INSERT INTO carts (date, status_code, buyer_id) values ("2022-01-27T15:13:31.253314", "aberto", 1), ("2023-01-27T15:13:31.253314", "fechado", 2),("2023-01-27T15:13:31.253314", "fechado", 3) ;
##ItemCart.java
INSERT INTO items_cart (quantity, cart_id, product_announcement_id) values (1, 1, 1), (20, 2, 2);
##Agent.java
INSERT INTO agents (name, section_id) values ("Agent1", 1),("Agent2", 2), ("Agent3", 3);
##Inbound.java
INSERT INTO inbounds (date, agent_id, section_id) values ("2022-01-27T15:13:31.253314", 1, 1),
("2022-01-27T15:13:31.253314", 2, 2),
("2022-01-27T15:13:31.253314", 3, 3);
##Batch.java
INSERT INTO batches (current_temperature, due_date, initial_quantity, manufacturing_date_time, product_id, stock, inbound_id, product_announcement_id)
values (7.0, "2022-01-27T15:13:31.253314", 10, "2023-01-27T15:13:31.253314", 1, 20, 1, 1), (7.0, "2022-01-27T15:13:31.253314", 10, "2023-01-27T15:13:31.253314", 1, 20, 2, 3);

###Grupo 11
## Mauro, Iberê, Lucas, Matheus e Stephanie`
``` 
