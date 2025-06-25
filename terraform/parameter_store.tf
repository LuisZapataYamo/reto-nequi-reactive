resource "aws_ssm_parameter" "db_name" {
  name  = "/${var.app_name}/db_name"
  type  = "String"
  value = var.db_name
}

resource "aws_ssm_parameter" "db_user" {
  name  = "/${var.app_name}/db_user"
  type  = "String"
  value = var.db_user
}

resource "aws_ssm_parameter" "db_port" {
  name  = "/${var.app_name}/db_port"
  type  = "String"
  value = tostring(var.db_port)
}

resource "aws_ssm_parameter" "server_port" {
  name  = "/${var.app_name}/server_port"
  type  = "String"
  value = tostring(var.server_port)
}

resource "aws_ssm_parameter" "db_host" {
  name  = "/${var.app_name}/db_host"
  type  = "String"
  value = aws_db_instance.postgres.address
}