package com.afl.aflcv.services;

import com.afl.aflcv.entities.Usuario;

public interface IUsuarioService {

	public Usuario findByUsername (String username);
}
