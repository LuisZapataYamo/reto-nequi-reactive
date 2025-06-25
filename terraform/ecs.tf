resource "aws_ecs_cluster" "webflux-cluster" {
  name = "${var.app_name}-cluster"
}

resource "aws_ecs_task_definition" "webflux_task" {
  family                   = "${var.app_name}-task"
  requires_compatibilities = ["FARGATE"]
  network_mode            = "awsvpc"
  cpu                     = "512"
  memory                  = "1024"
  execution_role_arn      = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = var.app_name
      image     = "${aws_ecr_repository.webflux_api.repository_url}:latest"
      portMappings = [
        {
          containerPort = var.server_port
          hostPort      = var.server_port
        }
      ]
      secrets = [
        {
          name      = "DB_NAME"
          valueFrom = aws_ssm_parameter.db_name.arn
        },
        {
          name      = "DB_USERNAME"
          valueFrom = aws_ssm_parameter.db_user.arn
        },
        {
          name      = "DB_PORT"
          valueFrom = aws_ssm_parameter.db_port.arn
        },
        {
          name      = "DB_PASSWORD"
          valueFrom = var.db_password_arn
        },
        {
          name      = "SERVER_PORT"
          valueFrom = aws_ssm_parameter.server_port.arn
        },
        {
          name      = "DB_HOST"
          valueFrom = aws_ssm_parameter.db_host.arn
        }
      ],
      environment = [
        {
          name  = "SSL_MODE"
          value = "require"
        }
      ]
    }
  ])
}

