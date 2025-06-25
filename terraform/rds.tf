resource "aws_db_instance" "postgres" {
  region = var.region
  identifier         = "${var.app_name}-postgres-db"
  engine             = "postgres"
  instance_class     = "db.t3.micro"
  allocated_storage  = 20
  db_name            = var.db_name
  username           = var.db_user
  password           = var.db_password
  port               = var.db_port
  skip_final_snapshot = true
  publicly_accessible = true
}

output "db_host" {
  value = aws_db_instance.postgres.address
}
