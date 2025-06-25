# Obtener la VPC por defecto
data "aws_vpc" "default" {
  default = true
}

# Obtener las subnets públicas asociadas a esa VPC
data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

resource "aws_security_group" "ecs_tasks" {
  region = ""
  name        = "${var.app_name}-sg"
  description = "Permitir acceso publico al contenedor"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    from_port   = var.server_port
    to_port     = var.server_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_ecs_service" "webflux_service" {
  name            = "${var.app_name}-service"
  cluster         = aws_ecs_cluster.webflux-cluster.id
  task_definition = aws_ecs_task_definition.webflux_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_tasks.id]
    assign_public_ip = true
  }

  depends_on = [
    aws_ecs_task_definition.webflux_task
  ]
}
