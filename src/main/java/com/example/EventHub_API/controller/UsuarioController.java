package com.example.EventHub_API.controller;

import com.example.EventHub_API.dto.UsuarioCreateDTO;
import com.example.EventHub_API.dto.UsuarioLoginDTO;
import com.example.EventHub_API.dto.UsuarioResponseDTO;
import com.example.EventHub_API.dto.TokenResponseDTO;
import com.example.EventHub_API.service.UsuarioService;
import com.example.EventHub_API.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService,
                             AuthenticationManager authenticationManager,
                             JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioCreateDTO registroDTO) {
        UsuarioResponseDTO usuarioSalvo = usuarioService.criarUsuario(registroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> loginUsuario(@Valid @RequestBody UsuarioLoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        Boolean organizador = usuarioService.buscarPorEmail(loginDTO.email()).getOrganizador();

        TokenResponseDTO response = new TokenResponseDTO(
                token,
                loginDTO.email(),
                organizador
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
    }
}