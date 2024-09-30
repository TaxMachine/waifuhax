{ pkgs ? import <nixpkgs> {} }:
pkgs.mkShell {
  nativeBuildInputs = with pkgs.buildPackages; [ jdk21_headless python3 ];

  buildInputs = [ pkgs.jdt-language-server ];

  shellHook = ''
    export JAVA_HOME=${pkgs.jdk21_headless}
    export JDTLS_HOME=${pkgs.jdt-language-server}
  '';
}
