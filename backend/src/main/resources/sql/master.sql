create table master_data_set(
  id BIGSERIAL PRIMARY KEY ,
  name varchar(255) NOT NULL DEFAULT '',
  path text NOT NULL DEFAULT  '',
  size DOUBLE PRECISION NOT NULL DEFAULT 0,
  amount int NOT NULL DEFAULT 0,
  type int NOT NULL DEFAULT 0
);

create table master_model(
  id BIGSERIAL PRIMARY KEY ,
  path text NOT NULL DEFAULT '',
  exe_file varchar(255) NOT NULL DEFAULT ''
);

create table master_mission(
  id BIGSERIAL PRIMARY KEY ,
  data_id bigint NOT NULL DEFAULT 0,
  model_id BIGINT NOT NULL DEFAULT 0,
  node_name  VARCHAR(255) NOT NULL DEFAULT '',
  timestamp BIGINT NOT NULL DEFAULT 0,
  status int NOT NULL DEFAULT 0, --0-等待 1-进行中 2-完成
  evaluate text NOT NULL DEFAULT ''
);
create table master_filed(
  id BIGSERIAL PRIMARY KEY ,
  name VARCHAR(255) NOT NULL DEFAULT '',
  introduction text NOT NULL DEFAULT ''
)