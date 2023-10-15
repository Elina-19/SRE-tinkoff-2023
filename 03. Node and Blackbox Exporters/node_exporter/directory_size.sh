*/1 * * * * root du -sb /var/log /var/cache/apt /var/lib/prometheus \
 | sed -ne 's/^\([0-9]\+\)\t\(.*\)$/node_directory_size_bytes{directory="\2"} \1/p' > /var/lib/node_exporter/textfile_collector/directory_size.prom.$$ \
  && mv /var/lib/node_exporter/textfile_collector/directory_size.prom.$$ /var/lib/node_exporter/textfile_collector/directory_size.prom