variable "app_name" {
  type        = string
  default     = "webflux-api"
  description = "Nombre base para recursos"
}

variable "db_name" {
  type        = string
  default     = "franchises"
}

variable "db_user" {
  type        = string
  default     = "postgres"
}

variable "db_password" {
  type        = string
  description = "Contraseña de la base de datos"
}

variable "db_port" {
  type    = number
  default = 5432
}


variable "server_port" {
  type    = number
  default = 8080
}

variable "region" {
  type    = string
  default = "us-east-1"
}

variable "db_password_arn" {
  type    = string
  default = "arn:aws:secretsmanager:us-east-1:156207843566:secret:passwordbd-ORq0Yn"
}

