provider "google" {
  region = "${var.region}"
}

resource "google_project" "project" {
  count           = "${var.create_project}"
  name            = "${var.project_name}"
  project_id      = "${var.project_id}"
  billing_account = "${var.billing_account}"
  org_id          = "${var.org_id}"
  # Lifecycle does not work if project exists
  lifecycle {
    ignore_changes = ["project"]
  }
}

resource "google_project_services" "project" {
  count    = "${var.create_project}"
  project  = "${var.project_id}"
  services = [
    "compute.googleapis.com"
  ]
}
