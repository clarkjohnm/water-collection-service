variable "project_name" {
  default = "water-collection-service"
}
variable "project_id" {
  default = "wcs-195520"
}
variable "billing_account" {
  default = "00D150-40A063-D504C5"
}
### NOTE that the TF_VAR_org_id is requried. Terraform doesn't use the value defined here so you might see the following error when you run terraform apply
### Error 400: field [Project.parent] has issue [Parent id must be numeric.]
variable "org_id" {
  default = "cybersapien.org"
}
variable "region" {
  default = "us-east1"
}
# Create project should be 0 (no) or 1 (yes)
variable "create_project" {
  default = 0
}
