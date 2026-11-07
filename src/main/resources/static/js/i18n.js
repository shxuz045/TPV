/**
 * TPV Restaurante ChinaTown - Sistema Bilingüe Completo (Español / 中文)
 * Proporciona traducción en tiempo real de toda la interfaz, cartas, comandas,
 * tablas de administración e historiales, preservando intactos números, teléfonos y nombres de calles.
 */
(function () {
    'use strict';

    // ── DICCIONARIO PRINCIPAL ESPAÑOL -> CHINO ──
    const DICT_ES_ZH = {
        // Cabecera y Navegación
        'Restaurante ChinaTown': '唐人街餐厅',
        'Usuario': '用户',
        'Sesión iniciada': '已登录',
        'Sala': '堂食',
        'SALA': '堂食',
        'Recogida': '打包',
        'RECOGIDA': '打包',
        'Domicilio': '外卖',
        'DOMICILIO': '外卖',
        'Gestión de Productos': '菜品管理',
        'Historial de Comandas': '订单历史',
        'Historial de Eliminaciones': '删除记录',
        'Historial de Cambios': '修改记录',
        'Salir de la cuenta': '退出登录',
        'Opciones de perfil': '用户选项',
        'Aviso': '温馨提示',
        'AVISO': '温馨提示',
        'Confirmación': '请确认操作',
        '¿Estás seguro?': '请确认操作',
        'Entendido': '知道了',
        'Guardado': '已保存',
        'Error': '操作提示',
        'Aceptar': '确定',
        'Cancelar': '取消',
        'Confirmar': '确认',
        'Cerrar': '关闭',
        'Guardar Cambios': '保存修改',
        'GUARDAR CAMBIOS': '保存修改',
        'Guardar': '保存',
        'GUARDAR': '保存',
        'Eliminar': '删除',
        'ELIMINAR': '删除',
        'Editar': '编辑',
        'EDITAR': '编辑',
        'Volver a Sala': '返回大厅',
        'Volver a existentes': '返回已有分类',
        'Sí, continuar': '确认继续',
        'Sí, borrar comanda': '确认整单删除',
        'Sí, borrar todo': '确认全部清空',
        'Sí, asignar mesa': '确认分配桌位',
        'Comanda borrada': '订单已删除',
        'Comanda movida': '订单转移成功',
        'Mesa asignada': '转桌成功',
        'Importe insuficiente': '收款金额不足',
        'Teléfono incorrecto': '电话号码格式有误',
        'Dirección requerida': '请填写送达地址',
        'Elección requerida': '请选择套餐选项',
        'Falta plato en el Menú': '套餐菜品未选齐',
        'Operación realizada con éxito.': '操作已成功完成。',

        // Mensajes de Alerta y Confirmación en lenguaje de hostelería natural
        'La comanda está vacía. Añade productos antes de cobrar.': '当前订单暂无菜品，请先点餐后再结账。',
        'La comanda está vacía. Añade productos antes de imprimir el ticket.': '当前订单暂无菜品，请先点餐后再打印小票。',
        '¿Estás seguro de que deseas borrar y cancelar esta comanda completa?': '确定要彻底删除并取消此整张订单吗？',
        'Esta acción no se puede deshacer.': '此操作无法撤销，请谨慎操作。',
        'La comanda ha sido borrada y cancelada correctamente.': '订单已成功整单取消并删除。',
        'Error de conexión al borrar la comanda.': '网络连接异常，删除订单失败。',
        'Error de conexión al cobrar la comanda.': '网络连接异常，结账失败。',
        'Error de conexión al guardar los datos.': '网络连接异常，保存配送信息失败。',
        'Error de conexión al eliminar la comanda.': '网络连接异常，删除历史订单失败。',
        'Error de conexión.': '网络连接异常，请重试。',
        'No hay mesas libres disponibles en sala.': '大厅暂无空闲可用桌位。',
        'El teléfono debe tener exactamente 9 dígitos numéricos.': '客户电话必须是9位有效纯数字，请核对后输入。',
        'El teléfono debe tener exactamente 9 dígitos numéricos (sin signos negativos ni letras).': '客户电话必须是9位有效纯数字，不能包含符号或字母。',
        'El teléfono debe contener exactamente 9 números positivos.': '客户电话必须输入9位纯数字。',
        'La dirección de entrega es obligatoria.': '送达地址为必填项，请输入客户地址。',
        'Selecciona o escribe una dirección de entrega.': '请选择或输入有效的送餐地址。',
        'Datos de entrega actualizados correctamente.': '配送信息已成功保存更新。',
        '¿Eliminar esta dirección guardada?': '确定要删除此条已保存的送达地址吗？',
        '¿Eliminar dirección?': '删除送达地址',
        '¿Borrar Comanda?': '删除整单',
        '¿Borrar comanda?': '删除整单',
        '¿Eliminar este plato definitivamente?': '确定要彻底删除该菜品吗？删除后将无法恢复。',
        '¿Borrar todo el historial?': '确定要清空所有历史记录吗？此操作无法恢复。',
        '¿Borrar todo el historial de cambios?': '确定要清空所有修改记录吗？此操作无法恢复。',
        '¿Borrar todo el historial de eliminaciones?': '确定要清空所有删除记录吗？此操作无法恢复。',
        '¿Revertir este cambio?': '确定要撤销此次修改并恢复先前状态吗？',
        '¿Restaurar este plato a la carta?': '确定要将此菜品重新恢复并上架到菜单中吗？',
        'Se asignará automáticamente el primer número de recogida libre.': '系统将自动分配空闲打包单号。',
        'Pedido cobrado y marcado como entregado.': '订单已结账并标记为已送达。',
        'Producto no encontrado.': '未找到对应菜品。',
        'No se pudo crear el producto: datos o precio no válidos.': '创建菜品失败：填写数据或价格无效。',
        'El precio es demasiado alto o inválido (máximo 9.999,99 €).': '菜品价格过高或格式无效（最高支持 9,999.99 €）。',
        'No se pudo actualizar el producto: el precio es demasiado alto.': '更新菜品失败：价格过高。',
        'Entrada de historial no encontrada.': '未找到对应历史记录。',
        'El producto ya no existe.': '该菜品已不存在。',
        'El producto no se encontró para restaurar.': '未找到需要恢复的菜品。',

        // Hub / Inicio
        'SELECCIONA SERVICIO': '选择服务类型',
        'Elige el tipo de pedido para comenzar': '请选择订单类型以开始点单',
        'MESAS 1 – 27': '1 – 27号桌',
        'Servicio en mesa del restaurante': '餐厅堂食服务',
        'ENTRAR A SALA →': '进入堂食 →',
        'PEDIDOS 40 – 89': '40 – 89号订单',
        'Pedidos para llevar en el local': '店内自取打包订单',
        'ENTRAR A RECOGIDA →': '进入打包 →',
        'PEDIDOS 90+': '90+号订单',
        'Reparto a domicilio con dirección y teléfono': '外卖送餐服务（含地址与电话）',
        'ENTRAR A DOMICILIO →': '进入外卖 →',

        // Sala y Mesas
        'MESAS DE SALA': '堂食桌位',
        'Mesas': '桌位',
        'Mesa': '桌',
        'Libre': '空闲',
        'Ocupada': '就餐中',
        'Pendiente de cobro': '待结账',
        'Pendiente': '待结账',
        'Total': '总额',
        'Total:': '总额:',
        'Nueva Comanda': '新建订单',
        'Cambiar a otra mesa': '更换桌位',
        'Comanda abierta': '已开台',
        'personas': '人',
        'comensales': '位顾客',
        'Abrir comanda': '开台点单',
        'Mesa libre': '空闲桌位',
        'Mesa ocupada': '占用桌位',
        'Ver Comanda': '查看订单',

        // Recogida
        '+ NUEVO PEDIDO DE RECOGIDA': '+ 新建打包订单',
        'PEDIDOS DE RECOGIDA ACTIVOS': '进行中的打包订单',
        'Sin pedidos de recogida': '暂无打包订单',
        'No hay pedidos de recogida activos en este momento': '当前没有进行中的打包订单',
        'Cliente': '客户',
        'Cliente:': '客户:',
        'Teléfono': '电话',
        'Teléfono:': '电话:',
        'Tel': '电话',
        'Tel:': '电话:',
        'Hora': '时间',
        'Hora:': '时间:',
        'Al momento': '尽快 / 立即',
        'Cobrar': '结账',
        'Ver comanda': '查看订单',
        'Cancelar pedido': '取消订单',
        'Pedido': '订单',

        // Domicilio
        '+ NUEVO PEDIDO A DOMICILIO': '+ 新建外卖订单',
        'PEDIDOS A DOMICILIO ACTIVOS': '进行中的外卖订单',
        'Sin pedidos a domicilio': '暂无外卖订单',
        'No hay pedidos a domicilio activos en este momento': '当前没有进行中的外卖订单',
        'Nuevo Pedido a Domicilio': '新建外卖订单',
        'Teléfono del cliente': '客户电话',
        'Buscar': '搜索',
        'Introduce los 9 dígitos del teléfono': '请输入9位电话号码',
        'Cliente nuevo': '新客户',
        'Cliente habitual': '老客户',
        'Nombre': '姓名',
        'Nombre:': '姓名:',
        'Nombre (opcional)': '客户姓名（选填）',
        'Dirección de entrega': '送达地址',
        'Dirección': '地址',
        'Dirección:': '地址:',
        'Direcciones guardadas': '已存地址',
        '+ Añadir nueva dirección': '+ 添加新地址',
        '+ Usar nueva dirección': '+ 使用新地址',
        'Horario de entrega': '送达时间',
        'Horario de entrega:': '送达时间:',
        'Lo antes posible': '尽快送达',
        'Lo antes posible (30-45 min)': '尽快送达 (30-45分钟)',
        'Programar hora': '预约时间',
        'Hora programada': '预约时间',
        'Notas de entrega': '配送备注',
        'Notas de entrega:': '配送备注:',
        'Notas de entrega (opcional)': '配送备注（选填）',
        'Notas para el repartidor (portal, piso, timbre...)': '配送备注（门牌、楼层、门铃...）',
        'Crear Pedido': '创建订单',
        'Crear pedido': '创建订单',
        'Crear comanda': '开始点单',
        'CREAR COMANDA A DOMICILIO': '创建外卖订单',
        'Continuar y Añadir Platos →': '下一步，去点餐 →',
        'Toca para entrar en la comanda y gestionar el cobro': '点击进入订单管理与结账',
        '⚠️ Sin platos añadidos todavía (pulsa para añadir)': '⚠️ 暂未添加菜品（点击添加）',
        'Editar Datos de Entrega': '修改配送信息',
        'Modifica el teléfono, nombre, dirección u horario': '修改客户电话、姓名、地址或送达时间',
        'Teléfono (9 dígitos)': '电话号码（9位）',
        'Pedido Cancelado': '订单已取消',
        'Para ahora (Inmediato)': '立即配送（尽快）',
        '⚡ Para ahora (Inmediato)': '⚡ 立即配送（尽快）',
        '📅 Programar hora': '📅 预约配送时间',
        '📞 Teléfono del cliente (9 dígitos) *': '📞 客户电话（9位）*',
        '📍 Selecciona dirección de entrega:': '📍 选择配送地址:',
        '📍 Dirección de entrega *': '📍 送达地址 *',
        '💬 Notas de entrega (opcional)': '💬 配送备注（选填）',
        'ℹ️ Cliente nuevo. Introduce sus datos:': 'ℹ️ 新客户。请输入配送信息:',
        '✏️ editar nombre': '✏️ 修改姓名',
        'Calle, número, piso...': '街道、门牌、楼层...',
        'Timbre roto, dejar en puerta...': '门铃损坏、放门口...',

        // Validaciones hora
        'Debes indicar una hora de entrega.': '请选择配送时间。',
        'Hora requerida': '配送时间为必填',

        // Comanda (POS)
        'Aparcar': '挂单',
        'APARCAR': '挂单',
        'Borrar Comanda': '删除整单',
        'BORRAR COMANDA': '删除整单',
        'Cambiar Mesa': '换桌',
        'CAMBIAR MESA': '换桌',
        'Pasar a Recogida': '转为打包',
        'PASAR A RECOGIDA': '转为打包',
        'Pasar a Mesa': '转为堂食',
        'PASAR A MESA': '转为堂食',
        'Pasar pedido a Recogida': '将订单转为打包',
        'Pasar pedido a Mesa de Sala': '将订单转为大厅桌位',
        'Pasar a Mesa de Sala': '转为大厅堂食桌位',
        'Cambiar de Mesa': '更换桌位',
        'Sí, asignar mesa': '确认分配桌位',
        'No hay mesas libres disponibles en sala.': '大厅暂无空闲可用桌位。',
        'Comanda movida': '订单已转移',
        'Mesa asignada': '已分配桌位',
        'PRECIO': '总额',
        'artículos': '件菜品',
        'artículo': '件菜品',
        'Servicio:': '服务:',
        'Servicio: SALA': '服务: 堂食',
        'Servicio: RECOGIDA': '服务: 打包',
        'Servicio: DOMICILIO': '服务: 外卖',
        '🖨️ TICKET': '🖨️ 打印小票',
        'COBRAR': '结账',
        'Aparcar comanda y volver': '挂单并返回',
        'Borrar y cancelar comanda completa': '整单删除并取消',
        'Cambiar a otra mesa libre': '更换至其他空闲桌位',
        'Imprimir ticket para llevar a la mesa': '打印小票送至桌位',
        'Cobrar y cerrar comanda': '结账并完成订单',
        'No hay productos en esta comanda.': '订单中暂无菜品。',
        'Pulsa un producto para añadirlo.': '点击右侧菜品加入订单。',
        'No hay productos en esta comanda.<br>Pulsa un producto para añadirlo.': '订单中暂无菜品。<br>点击右侧菜品加入。',
        'Eliminar plato': '删除菜品',
        'Quitar 1 unidad': '减少1份',
        'Añadir 1 unidad': '增加1份',
        'Toca para modificar opciones o comentarios': '点击修改备注或选项',

        // Modal Cobrar
        'Cobrar Comanda': '结账买单',
        'Total a cobrar:': '应收金额:',
        'Total de la comanda:': '订单总计:',
        'Método de pago': '支付方式',
        'Método de pago:': '支付方式:',
        'MÉTODO DE PAGO:': '支付方式:',
        'Efectivo': '现金',
        'EFECTIVO': '现金',
        'Tarjeta': '刷卡',
        'TARJETA': '刷卡',
        'Mixto': '混合支付',
        'MIXTO': '混合支付',
        'Importe entregado': '实收金额',
        'Importe entregado:': '实收金额:',
        'Cambio:': '找零:',
        'Cambio a devolver:': '找零金额:',
        'CAMBIO A DEVOLVER:': '找零金额:',
        'Confirmar Cobro': '确认结账',
        'CONFIRMAR COBRO': '确认结账',
        'Imprimir ticket': '打印小票',
        'IMPRIMIR TICKET Y COBRAR': '打印小票并结账',
        'SOLO COBRAR': '仅结账',
        'Elige método de pago y confirma el cobro de la comanda': '选择支付方式并确认结账',
        '¿Estás seguro de que deseas borrar y cancelar esta comanda completa?': '确定要彻底删除并取消此整张订单吗？',
        'Esta acción no se puede deshacer.': '此操作无法撤销。',
        'Sí, borrar comanda': '确定删除整单',

        // Modal Notas y Comentarios de cocina
        'Comentarios': '制作要求 / 备注',
        'Añade los ingredientes que no quieren o indicaciones especiales:': '添加特殊要求、忌口或口味偏好:',
        'Comentarios rápidos frecuentes:': '常用快捷备注:',
        'Sin cebolla': '不要葱',
        'Sin picante': '不要辣',
        'Poco picante': '微辣',
        'Muy picante': '特辣',
        'Sin gluten': '无麸质',
        'Sin sal': '少盐',
        'Salsa aparte': '酱汁分开',
        'Bien hecho': '炒透 / 做熟一点',
        'Para llevar': '打包',
        'Primero': '先上',
        'Segundo': '后上',
        'Escribir comentario personalizado:': '手动输入自定义备注:',
        'Guardar Comentario': '保存备注',
        'Guardar comentario': '保存备注',

        // Modales de Menús
        'Menú del Día': '今日特价套餐',
        'Elige los platos del menú': '请选择套餐内容',
        'Primer Plato': '第一道（前菜）',
        'PRIMER PLATO': '第一道（前菜）',
        'Segundo Plato': '第二道（主食）',
        'SEGUNDO PLATO': '第二道（主食）',
        'Tercer Plato': '第三道（主菜）',
        'TERCER PLATO': '第三道（主菜）',
        'Postre': '甜点',
        'POSTRE': '甜点',
        'Bebida': '饮料',
        'BEBIDA': '饮料',
        'Añadir a la comanda': '加入订单',
        'Menú Especial': '特色套餐',
        'Selecciona las opciones del menú': '请选择套餐选项',

        // Categorías de la Carta
        'Entrada': '前菜 / 点心',
        'Sopa': '汤类',
        'Arroz & Tallarines': '炒饭 & 炒面',
        'Tortilla & Verduras': '煎蛋 & 蔬菜',
        'Pescado & Gambas': '鲜鱼 & 虾类',
        'Ternera & Cerdo': '牛肉 & 猪肉',
        'Pollo': '鸡肉类',
        'Mariscos & Patos': '海鲜 & 烤鸭',
        'Platos Especiales': '特色菜品',
        'Menús Especiales': '特色套餐',
        'Bebidas': '饮料酒水',
        'Postres': '甜点',
        'Cafés': '咖啡茶饮',
        'General': '常规',
        'GENERAL': '常规',

        // Gestión de Productos (Admin)
        'Nº': '编号',
        'Plato': '菜品',
        'PLATO': '菜品',
        'Precio': '价格',
        'PRECIO': '价格',
        'Precio (€)': '价格 (€)',
        'Categoría': '分类',
        'CATEGORÍA': '分类',
        'Acciones': '操作',
        'ACCIONES': '操作',
        'BUSCADOR': '搜索',
        'LIMPIAR': '清空筛选',
        'Elegir categoría': '选择分类',
        'Todas las categorías': '全部分类',
        '➕ Crear nueva categoría...': '➕ 创建新分类...',
        '+ AÑADIR NUEVO PLATO': '+ 添加新菜品',
        '✏️ EDITAR PLATO': '✏️ 编辑菜品',
        '🗑️ BORRAR PLATO': '🗑️ 删除菜品',
        'Escribe el nombre del plato a buscar...': '输入要搜索的菜品名称...',
        'Escribe nueva categoría': '输入新分类名称',
        'Nueva categoría': '新分类名称',
        '+ Añadir': '+ 添加',
        'Expandir / Retraer todas': '展开 / 折叠全部',
        'Haz clic para expandir o retraer esta sección': '点击展开或折叠本分类',
        '¿Eliminar este plato definitivamente?': '确定要彻底删除该菜品吗？',
        'El número es obligatorio.': '编号为必填项。',
        'Número no válido (ej: 8, 8b, P1).': '编号格式无效（例如: 8, 8b, P1）。',
        'El nombre del plato es obligatorio.': '菜品名称为必填项。',
        'El precio es obligatorio.': '价格为必填项。',

        // Historial de Comandas
        'Consulta todas las comandas y elimina cualquier comanda que desees': '查询所有历史订单，支持按需删除指定订单',
        'Buscar mesa, plato, ID...': '搜索桌号、菜品、订单号...',
        'Todos los estados': '全部状态',
        'Cobradas (Cerradas)': '已结账（已关闭）',
        'Abiertas': '未结账（开台中）',
        'Canceladas': '已取消',
        'Todos los servicios': '全部服务',
        'Mesa y Recogida': '堂食与打包',
        'Solo Domicilio': '仅外卖',
        'Solo Sala': '仅堂食',
        'Solo Recogida': '仅打包',
        'COBRADA': '已结账',
        'ABIERTA': '未结账',
        'CANCELADA': '已取消',
        'Cobrada': '已结账',
        'Abierta': '未结账',
        'Cancelada': '已取消',
        'Pago: EFECTIVO': '支付: 现金',
        'Pago: TARJETA': '支付: 刷卡',
        'Pago: MIXTO': '支付: 混合',
        '🗑️ Borrar': '🗑️ 删除',
        'Abrir →': '进入 →',
        'Ver platos': '查看菜品',
        'No hay comandas registradas': '暂无历史订单记录',
        'Las comandas que se vayan creando aparecerán aquí.': '新创建的订单将会显示在这里。',

        // Historial de Cambios y Eliminaciones
        '📝 Historial de Cambios': '📝 菜品修改记录',
        '🗑️ Historial de Eliminaciones': '🗑️ 菜品删除记录',
        '🍽️ Gestión de Productos': '🍽️ 菜品管理',
        '🗑️ Ver Historial de Eliminaciones': '🗑️ 查看删除记录',
        '📝 Ver Historial de Cambios': '📝 查看修改记录',
        '🗑️ Borrar todo el historial': '🗑️ 清空所有记录',
        '¿Borrar todo el historial?': '确定清空所有记录吗？',
        'Esta acción eliminará todos los registros del historial de cambios.': '此操作将删除所有菜品修改历史记录。',
        'Esta acción eliminará todos los registros del historial de eliminaciones.': '此操作将删除所有菜品删除历史记录。',
        'No se puede deshacer.': '此操作无法撤销。',
        'Sí, borrar todo': '确定清空全部',
        'Registro de creaciones y modificaciones de platos con opción de revertir.': '记录菜品的新建与修改历史，支持一键撤销恢复。',
        'Aquí puedes ver los platos que han sido borrados de la carta y restaurarlos a la carta cuando lo desees.': '在此可查看从菜单删除的菜品，并可随时将其恢复到菜单中。',
        'No hay cambios registrados en el historial.': '暂无修改历史记录。',
        'No hay platos eliminados en el historial.': '暂无已删除的菜品记录。',
        'Ir a Gestión de Productos': '前往菜品管理',
        'Fecha': '日期',
        'Tipo': '类型',
        'Acción': '操作',
        'Fecha de eliminación': '删除日期',
        'Creado': '新增',
        'Modificado': '修改',
        '↩️ Revertir cambio': '↩️ 撤销此修改',
        '↩️ Restaurar plato': '↩️ 恢复此菜品',
        '¿Revertir este cambio?': '确定要撤销此项修改吗？',
        '¿Restaurar este plato a la carta?': '确定要将该菜品恢复到菜单中吗？',

        // Carta Oficial: 103 Platos ChinaTown
        'Ensalada china': '中华沙拉',
        'Ensalada de repollo': '卷心菜沙拉',
        'Rollo de primavera': '春卷',
        'Ensalada china con rollo de primavera': '中华沙拉加春卷',
        'Rollitos vietnamita': '越南春卷',
        'Wan-tun frito': '炸云吞',
        'Ensalada de gambas': '鲜虾沙拉',
        'Ensalada de pollo, piña, tomate y manzana': '鸡肉水果沙拉',
        'Pan de gambas': '炸虾片',
        'Cóctel de gambas': '鲜虾鸡尾沙拉',
        'Pan chino': '炸馒头 / 花卷',
        'Ensalada de algas wakame': '凉拌海草',
        'Shien ló Pao': '生煎包',
        'Shia Chiao': '水晶虾饺',
        'Sau Mai': '烧麦',
        'Kuotie': '锅贴',
        'Siau long pao': '小笼包',
        'Zil chil chaol': '鲜虾煎饺',
        'Gyozas de pollo a la plancha': '铁板鸡肉饺',
        'Empanadillas vegetales Shuijing': '水晶蔬菜饺',
        'Albóndigas de sepia y gambas': '虾仁墨鱼丸',
        'Samosa': '咖喱角',
        'Saumai de marisco': '海鲜烧麦',
        'Combinado 1: Sau Mai': '拼盘1: 烧麦',
        'Combinado 2: Vapor': '拼盘2: 蒸点拼盘',
        'Combinado 3: Fritos': '拼盘3: 炸点拼盘',
        'Sopa de la casa': '本楼招牌汤',
        'Sopa de aletas de tiburón': '鱼翅汤',
        'Sopa agria y picante': '酸辣汤',
        'Sopa de fideos con zha chai (picante)': '榨菜肉丝汤面',
        'Sopa de Wan-tun': '云吞汤',
        'Sopa de pollo con champiñones': '蘑菇鸡汤',
        'Sopa de nido de golondrinas': '燕窝汤',
        'Sopa de maíz con carne de cangrejo': '蟹肉玉米汤',
        'Sopa de marisco': '海鲜汤',
        'Arroz blanco': '白米饭',
        'Arroz frito con tres delicias': '三鲜炒饭',
        'Arroz frito con pollo y curry': '咖喱鸡炒饭',
        'Arroz frito con gambas': '虾仁炒饭',
        'Arroz frito al estilo yangchong': '扬州炒饭',
        'Arroz frito con salsa miso': '味噌炒饭',
        'Arroz frito con salsa tailandesa': '泰式炒饭',
        'Tallarines frito con tres delicias': '三鲜炒面',
        'Fideos de arroz chino': '炒米粉',
        'Tallarines de Pekín': '北京炸酱面',
        'Pasta de arroz': '炒河粉',
        'Fideos de arroz chinos con gambas': '虾仁炒米粉',
        'Fideos de sojas': '炒粉丝',
        'Tallarines con gambas': '虾仁炒面',
        'Tallarines chino a la plancha': '铁板炒面',
        'Tallarines udon con tres delicias': '三鲜乌冬面',
        'Tortilla con gambas': '虾仁煎蛋',
        'Tortilla con jamón': '火腿煎蛋',
        'Verdura china con setas': '香菇炒青菜',
        'Verduras variadas salteadas': '炒什锦蔬菜',
        'Bambú y setas chinas salteadas': '炒双冬（冬菇冬笋）',
        'Patatas fritas': '炸薯条',
        'Pescado frito con patatas o ensalada': '炸鱼配薯条/沙拉',
        'Pescado frito con cebolleta y jengibre': '姜葱鱼片',
        'Pescado agridulce': '糖醋鱼片',
        'Gambas con curry': '咖喱大虾',
        'Langostinos fritos': '炸大虾',
        'Gambas agridulce': '糖醋大虾',
        'Gambas en salsa a la plancha': '铁板大虾',
        'Gambas con sal y pimienta': '椒盐大虾',
        'Verduras con gambas': '蔬菜炒虾仁',
        'Gambas con bambú y setas': '双冬炒虾仁',
        'Gambas con salsa picante': '辣子虾仁',
        'Gambas con salsa de ostras': '蚝油虾仁',
        'Gambas con salsa tailandesa': '泰式大虾',
        'Ternera con salsa de curry': '咖喱牛肉',
        'Ternera con salsa tailandesa': '泰式牛肉',
        'Ternera con salsa de ostras': '蚝油牛肉',
        'Ternera con pimiento verde': '青椒牛肉',
        'Ternera con cebolla': '洋葱牛肉',
        'Ternera con champiñones': '蘑菇牛肉',
        'Ternera salteada picante': '辣炒牛肉',
        'Ternera con salsa miso': '味噌牛肉',
        'Verduras con ternera': '蔬菜牛肉',
        'Ternera con bambú y setas chinas': '双冬牛肉',
        'Ternera con patatas fritas': '薯条牛肉',
        'Ternera con salsa a la plancha': '铁板牛肉',
        'Cerdo agridulce': '糖醋里脊 / 古老肉',
        'Costillas asadas': '烤排骨',
        'Costillas agridulces': '糖醋排骨',
        'Cerdo con bambú y setas chinas': '双冬肉丝',
        'Pollo con salsa de curry': '咖喱鸡肉',
        'Pollo con salsa tailandesa': '泰式鸡肉',
        'Pollo con salsa picante': '辣子鸡丁',
        'Pollo con salsa miso': '味噌鸡肉',
        'Pollo con almendras': '杏仁鸡丁',
        'Verduras con pollo': '蔬菜鸡肉',
        'Pollo con piña (agridulce)': '菠萝鸡',
        'Pollo agridulce': '糖醋鸡块',
        'Pollo con champiñones': '蘑菇鸡肉',
        'Pollo con bambú y setas chinas': '双冬鸡肉',
        'Pollo con limón': '柠檬鸡',
        'Bolitas de pollo': '炸鸡球',
        'Pechuga de pollo rebozado': '香酥鸡排',
        'Pollo con salsa a la plancha': '铁板鸡肉',
        'Mariscos variados salteados': '炒什锦海鲜',
        'Mariscos especiales a la plancha': '铁板什锦海鲜',
        'Cangrejo especial a la plancha': '铁板螃蟹',
        'Sepia con pimientos y setas': '椒香青椒鱿鱼',
        'Sepia especial a la plancha': '铁板鱿鱼',
        'Pato asado a la pekinesa con crepes': '北京烤鸭配荷叶饼',
        'Pato con naranja': '橙汁鸭',
        'Pato a la plancha': '铁板烤鸭',
        'Pato con piña': '菠萝鸭',
        'Pato con limón': '柠檬鸭',
        'Pato con bambú y setas': '双冬鸭肉',
        'Ku-Bak tres delicias': '三鲜锅巴',
        'Ku-Bak con gambas': '虾仁锅巴',
        'Familia feliz a la plancha': '铁板全家福',
        'Entremeses variados': '什锦拼盘',
        'Ancas de ranas a la plancha': '铁板田鸡腿',
        'Ancas de ranas con sal y pimienta': '椒盐田鸡腿',
        'Ternera "China Town"': '唐人街招牌牛肉',
        'Hormigas subiendo al árbol Ku-Bak': '蚂蚁上树锅巴',
        'Bistec a la plancha': '铁板牛排',
        'Filete de pollo a la plancha': '铁板鸡排',
        'Menú 2 personas Tipo A': '2人套餐A',
        'Menú 2 personas Tipo B': '2人套餐B',
        'Menú 3 personas Tipo A': '3人套餐A',
        'Menú 3 personas Tipo B': '3人套餐B',
        'Menú 4 personas Tipo A': '4人套餐A',
        'Menú 4 personas Tipo B': '4人套餐B',
        'Menú 5 personas Tipo A': '5人套餐A',
        'Menú 5 personas Tipo B': '5人套餐B',
        'Menú 6 personas Tipo B': '6人套餐B',
        'Menú 7 personas Tipo A': '7人套餐A',
        'Menú 8 personas Tipo B': '8人套餐B',
        'Menú 10 personas Tipo B': '10人套餐B',
        'Menú del Día Estándar': '今日特惠套餐',
        'Menú del Día Premium': '今日尊享特惠套餐',

        // Login
        'Iniciar sesión': '登录系统',
        'Usuario o contraseña incorrectos.': '用户名或密码不正确。',
        'Has cerrado sesión correctamente.': '已成功退出登录。',
        'Introduce tu usuario (admin o cajero)': '请输入用户名（admin 或 cajero）',
        'Introduce tu contraseña': '请输入密码',
        'Entrar': '登录',
        'Contraseña': '密码'
    };

    // Mapa inverso Chino -> Español
    const DICT_ZH_ES = {};
    Object.keys(DICT_ES_ZH).forEach(es => {
        DICT_ZH_ES[DICT_ES_ZH[es]] = es;
    });

    /**
     * Comprueba si una cadena es una dirección de calle o un número puro,
     * los cuales NO deben traducirse según la petición del usuario.
     */
    function esCalleONumero(str) {
        if (!str) return true;
        const s = str.trim();

        // Número puro (con o sin decimales, símbolos monetarios o telefónicos)
        if (/^#?\d+[\d\s.,\-]*€?$/.test(s)) return true;
        // Teléfono o código
        if (/^\+?\d{8,15}$/.test(s)) return true;

        // Comprobación de patrones de calles y direcciones españolas
        const regexDireccion = /^(Calle|C\/|Avda|Avenida|Plaza|Pz|Paseo|Ps|Camino|Ctra|Carretera|Bda|Barriada|Ronda|Travesía|Piso|Puerta|Bloque|Portal)\b/i;
        if (regexDireccion.test(s)) return true;

        return false;
    }

    /**
     * Traduce un texto concreto según el idioma objetivo.
     */
    function traducirTexto(texto, lang) {
        if (!texto || typeof texto !== 'string') return texto;
        const targetLang = lang || getIdiomaActual();
        const trimmed = texto.trim();
        if (!trimmed) return texto;

        if (targetLang === 'es') {
            if (DICT_ZH_ES[trimmed]) return texto.replace(trimmed, DICT_ZH_ES[trimmed]);
            return texto;
        }

        // --- TRADUCCIÓN A CHINO (zh) ---
        // 0. Si contiene saltos de línea (\n), traducir línea a línea para modales y alertas multilínea
        if (trimmed.includes('\n')) {
            const lineas = trimmed.split('\n');
            const trads = lineas.map(l => {
                const lt = l.trim();
                return lt ? traducirTexto(lt, targetLang) : l;
            });
            return texto.replace(trimmed, trads.join('\n'));
        }

        // 0b. Si es una calle o número, no se traduce
        if (esCalleONumero(trimmed)) {
            return texto;
        }

        // 1. Coincidencia directa completa
        if (DICT_ES_ZH[trimmed]) {
            return texto.replace(trimmed, DICT_ES_ZH[trimmed]);
        }

        // 2. Coincidencia insensible a mayúsculas
        const upper = trimmed.toUpperCase();
        for (const [key, val] of Object.entries(DICT_ES_ZH)) {
            if (key.toUpperCase() === upper) {
                return texto.replace(trimmed, val);
            }
        }

        // 3. Extracción de prefijo de icono/emoji: ej "🗑️ Borrar", "✏️ EDITAR PLATO", "🍽️ Gestión de Productos"
        try {
            const matchIcono = trimmed.match(/^([\u2600-\u27BF\uFE00-\uFE0F\uD83C-\uD83F][\uDC00-\uDFFF]?|\s|[•*+–—→↩️⚠️ℹ️✓❌✏️🗑️🍽️💬📍📞⏰❓])+([\s\S]+)$/);
            if (matchIcono) {
                const iconoPref = matchIcono[1];
                const resto = matchIcono[2].trim();
                if (DICT_ES_ZH[resto]) {
                    return texto.replace(trimmed, iconoPref + DICT_ES_ZH[resto]);
                }
                // Probar en mayúsculas también
                const restoUpper = resto.toUpperCase();
                for (const [key, val] of Object.entries(DICT_ES_ZH)) {
                    if (key.toUpperCase() === restoUpper) {
                        return texto.replace(trimmed, iconoPref + val);
                    }
                }
            }
        } catch (e) { }

        // 4. Extracción de sufijo de dos puntos ":" (ej "Cliente:", "Tel:", "Dirección:", "Total:")
        if (trimmed.endsWith(':')) {
            const sinDosPuntos = trimmed.slice(0, -1).trim();
            if (DICT_ES_ZH[sinDosPuntos]) {
                return texto.replace(trimmed, DICT_ES_ZH[sinDosPuntos] + ':');
            }
        }

        // 5. Detección de platos con número inicial: ej "1. Ensalada china", "3a. Ensalada...", "P1. Bistec..."
        const matchNum = trimmed.match(/^([A-Za-z0-9]+[.\s\-]+)(.+)$/);
        if (matchNum) {
            const prefijo = matchNum[1];
            const resto = matchNum[2].trim();
            if (DICT_ES_ZH[resto]) {
                return texto.replace(trimmed, prefijo + DICT_ES_ZH[resto]);
            }
        }

        // 6. Patrones dinámicos comunes
        // Alerta de importe entregado insuficiente
        const matchImporte = trimmed.match(/El dinero entregado \(([\d.,]+ €)\) es inferior al total a pagar \(([\d.,]+ €)\)/i);
        if (matchImporte) {
            return `实收金额（${matchImporte[1]}）不足，低于应付总额（${matchImporte[2]}），请核对收款金额。`;
        }
        // Alerta de comanda/pedido movido o transferido
        const matchMovido = trimmed.match(/Pedido movido a (Recogida \d+|Mesa \d+) correctamente\.?/i);
        if (matchMovido) {
            const dest = traducirTexto(matchMovido[1], 'zh');
            return `订单已成功转为 ${dest}。`;
        }
        const matchTransferido = trimmed.match(/Pedido transferido a (Mesa \d+) correctamente\.?/i);
        if (matchTransferido) {
            const dest = traducirTexto(matchTransferido[1], 'zh');
            return `订单已成功转移至 ${dest}。`;
        }
        // Alerta de comanda eliminada
        const matchEliminada = trimmed.match(/Comanda #(\d+) eliminada correctamente\.?/i);
        if (matchEliminada) {
            return `历史订单 #${matchEliminada[1]} 已成功删除。`;
        }
        // Confirmaciones de pasar a recogida / mesa
        if (trimmed.startsWith('¿Eliminar esta dirección guardada?')) {
            return trimmed.replace('¿Eliminar esta dirección guardada?', '确定要删除此条已保存的送达地址吗？');
        }
        if (/¿Pasar el pedido de .+ a Recogida\?/i.test(trimmed)) {
            return '确定将该订单转为打包自取吗？系统将自动分配空闲打包单号。';
        }
        const matchMoverMesa = trimmed.match(/¿(Pasar|Mover) el pedido de (.+) a (la )?(Mesa \d+)\?/i);
        if (matchMoverMesa) {
            const mDest = traducirTexto(matchMoverMesa[4], 'zh');
            return `确定要将订单转移至 ${mDest} 吗？`;
        }
        // Cancelación con éxito
        const matchCancelado = trimmed.match(/^El (.+) ha sido cancelado con éxito\.?$/i);
        if (matchCancelado) {
            const serv = traducirTexto(matchCancelado[1], 'zh');
            return `${serv} 已成功取消。`;
        }
        // Errores con prefijo dinámico
        const matchErrRecogida = trimmed.match(/^No se pudo mover a recogida:\s*(.*)$/i);
        if (matchErrRecogida) {
            return `转为打包失败：${matchErrRecogida[1]}`;
        }
        const matchErrMesa = trimmed.match(/^No se pudo mover a la mesa:\s*(.*)$/i);
        if (matchErrMesa) {
            return `转桌失败：${matchErrMesa[1]}`;
        }
        const matchErrBorrar = trimmed.match(/^Error al borrar la comanda:\s*(.*)$/i);
        if (matchErrBorrar) {
            return `删除订单失败：${matchErrBorrar[1]}`;
        }
        const matchErrCobrar = trimmed.match(/^Error al cobrar la comanda:\s*(.*)$/i);
        if (matchErrCobrar) {
            return `结账失败：${matchErrCobrar[1]}`;
        }
        const matchErrActualizar = trimmed.match(/^Error al actualizar datos:\s*(.*)$/i);
        if (matchErrActualizar) {
            return `更新配送信息失败：${matchErrActualizar[1]}`;
        }
        const matchErrEliminar = trimmed.match(/^Error al eliminar la comanda:\s*(.*)$/i);
        if (matchErrEliminar) {
            return `删除历史订单失败：${matchErrEliminar[1]}`;
        }
        // Mensajes de confirmación y alertas de pedidos
        const matchRegPedido = trimmed.match(/^Pedido\s+(Mesa \d+|Recogida \d+|Domicilio \d+)\s+registrado\.\s*Añade los platos a continuación\.?$/i);
        if (matchRegPedido) {
            const dest = traducirTexto(matchRegPedido[1], 'zh');
            return `订单 ${dest} 已成功开单登记，请在下方选择菜品。`;
        }
        const matchFase = trimmed.match(/^Pedido actualizado a:\s*(.*)$/i);
        if (matchFase) {
            return `订单状态已更新为：${traducirTexto(matchFase[1], 'zh')}`;
        }
        const matchProdCreado = trimmed.match(/^Producto\s*"(.*?)"\s*creado correctamente en\s*"(.*?)"\.?$/i);
        if (matchProdCreado) {
            return `菜品 “${matchProdCreado[1]}” 已成功创建于分类 “${traducirTexto(matchProdCreado[2], 'zh')}”。`;
        }
        const matchProdAct = trimmed.match(/^Producto actualizado correctamente a\s*"(.*?)"\.?$/i);
        if (matchProdAct) {
            return `菜品已成功更新为 “${matchProdAct[1]}”。`;
        }
        const matchProdElim = trimmed.match(/^"(.*?)"\s*eliminado\.?$/i);
        if (matchProdElim) {
            return `菜品 “${matchProdElim[1]}” 已成功删除。`;
        }
        const matchCreacionRev = trimmed.match(/^Creación revertida:\s*"(.*?)"\s*eliminado\.?$/i);
        if (matchCreacionRev) {
            return `已撤销新增操作：菜品 “${matchCreacionRev[1]}” 已删除。`;
        }
        const matchModRev = trimmed.match(/^Modificación revertida:\s*restaurado a\s*"(.*?)"\.?$/i);
        if (matchModRev) {
            return `已撤销修改操作：菜品已恢复为 “${matchModRev[1]}”。`;
        }
        const matchElimRev = trimmed.match(/^Eliminación revertida:\s*"(.*?)"\s*restaurado en la carta\.?$/i);
        if (matchElimRev) {
            return `已撤销删除操作：菜品 “${matchElimRev[1]}” 已重新上架。`;
        }
        const matchEleccion = trimmed.match(/^Por favor selecciona una opción en:\s*(.*)$/i);
        if (matchEleccion) {
            return `请选择必填项：${traducirTexto(matchEleccion[1], 'zh')}`;
        }
        if (/^Te falta(n por)? elegir/i.test(trimmed)) {
            let msgFalta = trimmed
                .replace(/^Te falta elegir el plato:\s*/i, '套餐菜品尚未选齐，请先选择：')
                .replace(/^Te faltan por elegir los platos:\s*/i, '套餐菜品尚未选齐，请先选择：')
                .replace(/\.?$/, '。')
                .replace(/Primer Plato/gi, '第一道（前菜）')
                .replace(/Segundo Plato/gi, '第二道（主食）')
                .replace(/Tercer Plato/gi, '第三道（主菜）')
                .replace(/Postre/gi, '甜点')
                .replace(/Bebida/gi, '饮料');
            return msgFalta;
        }
        if (/^Falta por elegir:/i.test(trimmed)) {
            let faltaMsg = trimmed
                .replace(/^Falta por elegir:\s*/i, '套餐菜品尚未选齐，缺少：\n')
                .replace(/-\s*PRIMERO/gi, '- 第一道（前菜）')
                .replace(/-\s*SEGUNDO/gi, '- 第二道（主食）')
                .replace(/-\s*TERCERO/gi, '- 第三道（主菜）')
                .replace(/-\s*POSTRE/gi, '- 甜点')
                .replace(/-\s*BEBIDA/gi, '- 饮料');
            return faltaMsg;
        }

        // "Ver platos (X artículos)" o "Ver platos ("
        if (/^Ver platos\s*\(/i.test(trimmed)) {
            return texto.replace(/Ver platos\s*\(/i, '查看菜品 (');
        }
        if (/^\)?\s*artículos?\)?$/i.test(trimmed)) {
            return texto.replace(/artículos?/i, '件菜品');
        }
        // Mesas: "Mesa 12" -> "12号桌"
        if (/^Mesa\s+(\d+)$/i.test(trimmed)) {
            return texto.replace(/^Mesa\s+(\d+)$/i, '$1号桌');
        }
        // Domicilios: "Domicilio 90" -> "外卖 90号"
        if (/^Domicilio\s+(\d+)$/i.test(trimmed)) {
            return texto.replace(/^Domicilio\s+(\d+)$/i, '外卖 $1号');
        }
        // Recogidas: "Recogida 40" -> "打包 40号"
        if (/^Recogida\s+(\d+)$/i.test(trimmed)) {
            return texto.replace(/^Recogida\s+(\d+)$/i, '打包 $1号');
        }
        // Artículos: "3 artículos" -> "3件菜品"
        if (/^(\d+)\s+artículos?$/i.test(trimmed)) {
            return texto.replace(/^(\d+)\s+artículos?$/i, '$1件菜品');
        }
        // Servicio
        if (/^Servicio:\s*SALA$/i.test(trimmed)) return '服务: 堂食';
        if (/^Servicio:\s*RECOGIDA$/i.test(trimmed)) return '服务: 打包';
        if (/^Servicio:\s*DOMICILIO$/i.test(trimmed)) return '服务: 外卖';

        // Pago en historial
        if (/^Pago:\s*(EFECTIVO|TARJETA|MIXTO)$/i.test(trimmed)) {
            const m = trimmed.match(/^Pago:\s*(.+)$/i);
            const tipo = m[1].toUpperCase();
            const tipoZh = tipo === 'EFECTIVO' ? '现金' : (tipo === 'TARJETA' ? '刷卡' : '混合');
            return '支付方式: ' + tipoZh;
        }

        // Notas con platos de menú: "1º: ... | 2º: ... | Postre: ..."
        if (trimmed.includes('1º:') || trimmed.includes('2º:') || trimmed.includes('Postre:') || trimmed.includes('Elección:')) {
            let res = trimmed
                .replace(/1º:/g, '第1道:')
                .replace(/2º:/g, '第2道:')
                .replace(/3º:/g, '第3道:')
                .replace(/Postre:/g, '甜点:')
                .replace(/Bebida:/g, '饮料:')
                .replace(/Elección:/g, '选项:')
                .replace(/Eleccion:/g, '选项:');
            Object.keys(DICT_ES_ZH).forEach(k => {
                if (k.length > 3 && res.includes(k)) {
                    res = res.split(k).join(DICT_ES_ZH[k]);
                }
            });
            return texto.replace(trimmed, res);
        }

        return texto;
    }

    /**
     * Obtiene el idioma actualmente guardado ('es' o 'zh').
     */
    function getIdiomaActual() {
        try {
            const guardado = localStorage.getItem('tpv_idioma');
            if (guardado === 'zh' || guardado === 'es') return guardado;
        } catch (e) { }
        return 'es';
    }

    /**
     * Aplica la traducción a un nodo DOM específico de forma recursiva.
     */
    function traducirNodo(node, lang) {
        if (!node) return;

        // 1. Nodo de texto
        if (node.nodeType === Node.TEXT_NODE) {
            const raw = node.nodeValue;
            if (!raw || !raw.trim()) return;

            if (lang === 'zh') {
                if (node._origValue === undefined) {
                    node._origValue = raw;
                }
                const traducido = traducirTexto(node._origValue, 'zh');
                if (traducido !== node.nodeValue) {
                    node.nodeValue = traducido;
                }
            } else if (lang === 'es' && node._origValue !== undefined) {
                node.nodeValue = node._origValue;
            }
            return;
        }

        // 2. Elemento HTML
        if (node.nodeType === Node.ELEMENT_NODE) {
            const tag = node.tagName.toLowerCase();
            if (tag === 'script' || tag === 'style' || tag === 'noscript' || tag === 'svg') return;
            if (node.classList && node.classList.contains('no-translate')) return;

            // Placeholder de inputs
            if (node.placeholder) {
                if (lang === 'zh') {
                    if (!node.getAttribute('data-orig-placeholder')) {
                        node.setAttribute('data-orig-placeholder', node.placeholder);
                    }
                    const orig = node.getAttribute('data-orig-placeholder');
                    node.placeholder = traducirTexto(orig, 'zh');
                } else if (lang === 'es' && node.getAttribute('data-orig-placeholder')) {
                    node.placeholder = node.getAttribute('data-orig-placeholder');
                }
            }

            // Atributo title
            if (node.title) {
                if (lang === 'zh') {
                    if (!node.getAttribute('data-orig-title')) {
                        node.setAttribute('data-orig-title', node.title);
                    }
                    const orig = node.getAttribute('data-orig-title');
                    node.title = traducirTexto(orig, 'zh');
                } else if (lang === 'es' && node.getAttribute('data-orig-title')) {
                    node.title = node.getAttribute('data-orig-title');
                }
            }

            // Atributo value en botones <input type="button|submit">
            if ((tag === 'input') && (node.type === 'button' || node.type === 'submit')) {
                if (lang === 'zh') {
                    if (!node.getAttribute('data-orig-value')) {
                        node.setAttribute('data-orig-value', node.value);
                    }
                    const orig = node.getAttribute('data-orig-value');
                    node.value = traducirTexto(orig, 'zh');
                } else if (lang === 'es' && node.getAttribute('data-orig-value')) {
                    node.value = node.getAttribute('data-orig-value');
                }
            }

            // Traducir todos los hijos recursivamente
            const children = node.childNodes;
            for (let i = 0; i < children.length; i++) {
                traducirNodo(children[i], lang);
            }
        }
    }

    /**
     * Aplica el idioma a todo el documento.
     */
    function aplicarIdioma(lang) {
        const targetLang = (lang === 'zh') ? 'zh' : 'es';
        try {
            localStorage.setItem('tpv_idioma', targetLang);
            document.cookie = 'tpv_idioma=' + targetLang + '; path=/; max-age=31536000';
        } catch (e) { }

        document.documentElement.lang = targetLang;

        actualizarBotonesSelector(targetLang);

        if (observer) observer.disconnect();

        traducirNodo(document.body, targetLang);

        iniciarObserver();

        window.dispatchEvent(new CustomEvent('idiomaCambiado', { detail: { idioma: targetLang } }));
    }

    /**
     * Actualiza el estado visual de los botones del selector en la cabecera.
     */
    function actualizarBotonesSelector(lang) {
        const btnEs = document.getElementById('lang-btn-es');
        const btnZh = document.getElementById('lang-btn-zh');
        if (btnEs && btnZh) {
            if (lang === 'zh') {
                btnZh.classList.add('active');
                btnEs.classList.remove('active');
            } else {
                btnEs.classList.add('active');
                btnZh.classList.remove('active');
            }
        }
    }

    // ── MUTATION OBSERVER PARA ELEMENTOS DINÁMICOS ──
    let observer = null;
    let debounceTimer = null;

    function iniciarObserver() {
        if (observer) observer.disconnect();
        const idioma = getIdiomaActual();
        if (idioma !== 'zh') return;

        observer = new MutationObserver(function (mutations) {
            if (debounceTimer) clearTimeout(debounceTimer);
            debounceTimer = setTimeout(function () {
                if (observer) observer.disconnect();
                mutations.forEach(function (m) {
                    if (m.type === 'childList') {
                        m.addedNodes.forEach(function (n) {
                            traducirNodo(n, 'zh');
                        });
                    } else if (m.type === 'characterData') {
                        traducirNodo(m.target, 'zh');
                    }
                });
                iniciarObserver();
            }, 50);
        });

        if (document.body) {
            observer.observe(document.body, {
                childList: true,
                subtree: true,
                characterData: true
            });
        }
    }

    /**
     * Alterna entre Español y Chino (si está en 'zh' pasa a 'es', y viceversa).
     */
    function alternarIdioma() {
        const actual = getIdiomaActual();
        const nuevo = (actual === 'zh') ? 'es' : 'zh';
        aplicarIdioma(nuevo);
    }

    // ── API PÚBLICA GLOBAL ──
    window.traducirTexto = traducirTexto;
    window.aplicarIdioma = aplicarIdioma;
    window.alternarIdioma = alternarIdioma;
    window.getIdiomaActual = getIdiomaActual;
    window.cambiarIdioma = function (lang) {
        if (!lang) {
            alternarIdioma();
        } else {
            aplicarIdioma(lang);
        }
    };

    // ── INICIALIZACIÓN AL CARGAR LA PÁGINA ──
    function init() {
        const idioma = getIdiomaActual();
        actualizarBotonesSelector(idioma);
        if (idioma === 'zh') {
            aplicarIdioma('zh');
        } else {
            actualizarBotonesSelector('es');
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
