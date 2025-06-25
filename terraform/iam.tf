# ARN hardcodeado del secreto en Secrets Manager
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "${var.app_name}-ecs-task-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

# Política estándar para ECS (logs y ECR)
resource "aws_iam_role_policy_attachment" "ecs_execution_role_standard" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# Política personalizada para acceso a parámetros SSM y Secrets Manager
resource "aws_iam_policy" "ecs_parameter_and_secret_access" {
  name = "${var.app_name}-ecs-param-secret-access"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "ssm:GetParameter",
          "ssm:GetParameters"
        ],
        Resource = [
          aws_ssm_parameter.db_name.arn,
          aws_ssm_parameter.db_user.arn,
          aws_ssm_parameter.db_port.arn,
          aws_ssm_parameter.server_port.arn,
          aws_ssm_parameter.db_host.arn
        ]
      },
      {
        Effect = "Allow",
        Action = [
          "secretsmanager:GetSecretValue"
        ],
        Resource = var.db_password_arn
      }
    ]
  })
}

# Adjuntar la política personalizada al rol
resource "aws_iam_role_policy_attachment" "ecs_custom_policy_attachment" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.ecs_parameter_and_secret_access.arn
}
