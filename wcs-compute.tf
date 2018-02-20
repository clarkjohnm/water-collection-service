data "google_compute_zones" "available" {}

resource "random_id" "compute_id" {
  byte_length = 4
  prefix      = "compute-"
}

resource "google_compute_instance" "default" {
  project = "${var.project_name}"
  zone    = "${data.google_compute_zones.available.names[0]}"
  name    = "${var.project_name}-${random_id.compute_id.hex}"
  machine_type = "g1-small"
  boot_disk {
    initialize_params {
      image = "cos-stable-64-10176-62-0"
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
  metadata {
    # This key/value pair does not work. Terraform complains that the interpolation for name is not allowed.
    gce-container-declaration = "spec:  containers:    - name: ${google_compute_instance.default.name}      image: 'gcr.io/wcs-195520/wcs:0.0.1-SNAPSHOT'      stdin: false      tty: false  restartPolicy: Never"
  }
}

output "instance_id" {
  value = "${google_compute_instance.default.self_link}"
}