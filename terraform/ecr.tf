resource "aws_ecr_repository" "webflux_api" {
  region = var.region
  name = var.app_name
}

